package com.arunava.example.nasaastronomypictureoftheday.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.arunava.example.nasaastronomypictureoftheday.databinding.ActivityMainBinding
import com.arunava.example.nasaastronomypictureoftheday.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val date by lazy { intent.getStringExtra("date") }

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.pictureOfTheDay.observe(this) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, it.error?.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
            binding.apply {
                title.text = it.data?.title
                description.text = it.data?.explanation
                if (it.data?.cachedImagePath != null) {
                    image.load(File(it.data.cachedImagePath))
                } else {
                    val request = ImageRequest.Builder(this@MainActivity)
                        .data(it.data?.url)
                        .target { drawable ->
                            image.setImageDrawable(drawable)
                            viewModel.saveImage(drawable.toBitmap(), cacheDir)
                        }
                        .build()
                    this@MainActivity.imageLoader.enqueue(request)
                }
            }
        }

        viewModel.getPictureOfTheDay(date)
    }
}