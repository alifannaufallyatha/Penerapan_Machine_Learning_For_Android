package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.getImageUri
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var cropImage: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                imageClassifierHelper = ImageClassifierHelper(
                    context = this,
                    classifierListener = object : ImageClassifierHelper.ClassifierListener {
                        override fun onError(error: String) {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                            runOnUiThread {
                                results?.let { it ->
                                    if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                        println(it)
                                        val sortedCategories =
                                            it[0].categories.sortedByDescending { it?.score }
                                        val displayResult =
                                            sortedCategories.joinToString(" ") {
                                                "${it.label} " + NumberFormat.getPercentInstance()
                                                    .format(it.score).trim()
                                            }
                                        val confidenceScore = sortedCategories.joinToString(" ") {
                                            NumberFormat.getPercentInstance()
                                                .format(it.score)
                                        }
                                        val textResult = "Hasil Analisis : ${displayResult}"

                                        analyzeImage(uri, textResult, confidenceScore)

                                    } else {
                                        Log.d("Testing", "Hasil Not Found")
                                    }
                                }
                            }
                        }
                    }
                )
                imageClassifierHelper.classifyImage(bitmap)
            } ?: run {
                showToast(getString(R.string.warning))
            }
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                currentImageUri = uri
                showImage()
                startUCrop(uri)
            } ?: showToast("Failed to get image URI")
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(this)
    }


    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri, result: String, score: String) {

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        intent.putExtra("Hasil", result)
        intent.putExtra("Score", score)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_item, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.historyBtn -> {
                startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == 101){
            val result: String = data!!.getStringExtra("Crop Image").toString()
//            var uri = data
            if (data != null) {
                currentImageUri = Uri.parse(result)
                showImage()
            }
        }
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}