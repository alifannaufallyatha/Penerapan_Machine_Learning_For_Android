package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.database.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.database.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import com.dicoding.asclepius.viewmodel.ViewModelHistory
import com.dicoding.asclepius.viewmodel.ViewModelNews
import java.util.Date

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val textResult = intent.getStringExtra("Hasil")
        val score = intent.getStringExtra("Score")
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
            binding.resultText.text = textResult
        }

        val newsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ViewModelNews::class.java)

        val historyViewModel by viewModels<ViewModelHistory>() {
            ViewModelFactory.getInstance(application)
        }

        historyViewModel.getHistoryFromLocal().observe(this){ history ->
            binding.buttonSave.setOnClickListener{
                historyViewModel.insertHistoryToLocal(PredictionHistoryEntity(imageUri.toString(), textResult.toString(), score.toString(), Date().toString()))
                Toast.makeText(this, "Berhasil Menyimpan Prediksi", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@ResultActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.rvNewsItems.layoutManager = LinearLayoutManager(this)
        binding.rvNewsItems.setHasFixedSize(true)
        binding.rvNewsItems.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager(this).orientation))

        newsViewModel.getNews()
        newsViewModel.listNews.observe(this){
            setNewsData(it)
        }

        newsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        newsViewModel.errorResponse.observe(this) {
            showError(it)
        }

    }

    private fun setNewsData(listNews: List<ArticlesItem>) {
        val adapter = NewsAdapter{
                selectedNews ->
            startActivity(Intent(this@ResultActivity, ResultActivity::class.java))
        }
        adapter.submitList(listNews)
        binding.rvNewsItems.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

}