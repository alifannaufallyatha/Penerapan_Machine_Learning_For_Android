package com.dicoding.asclepius.view

import android.gesture.Prediction
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.PredictionHistoryAdapter
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import com.dicoding.asclepius.viewmodel.ViewModelHistory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyAdapter = PredictionHistoryAdapter()

        val historyViewModel by viewModels<ViewModelHistory>() {
            ViewModelFactory.getInstance(application)
        }

        historyViewModel.getHistoryFromLocal().observe(this){ history ->
            if (history != null){
                historyAdapter.submitList(history)
            }
        }

        binding?.rvPredictHistory?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = historyAdapter
        }
    }
}