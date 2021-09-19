package com.arunava.example.nasaastronomypictureoftheday.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.arunava.example.nasaastronomypictureoftheday.databinding.ActivityMainBinding
import com.arunava.example.nasaastronomypictureoftheday.util.Resource
import com.arunava.example.nasaastronomypictureoftheday.util.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val date by lazy { intent.getStringExtra("date") }

    private val viewModel: MainViewModel by viewModels { ViewModelFactory }

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
                    Glide.with(this@MainActivity)
                        .load(File(it.data.cachedImagePath))
                        .into(image)
                } else {
                    Glide.with(this@MainActivity)
                        .load(it.data?.url)
                        .into(object : CustomTarget<Drawable>(){
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                binding.image.setImageDrawable(resource)
                                viewModel.saveImage(resource.toBitmap(), cacheDir)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        }

        viewModel.getPictureOfTheDay(date)
    }
}