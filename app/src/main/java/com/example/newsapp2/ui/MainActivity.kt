package com.example.newsapp2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.newsapp2.databinding.ActivityMainBinding
import com.example.newsapp2.db.ArticleRoomDatabase
import com.example.newsapp2.repositories.NewsRepository
import com.example.newsapp2.viewmodels.NewsViewModel
import com.example.newsapp2.viewmodels.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository=NewsRepository(ArticleRoomDatabase(this))
        val newsViewModelProviderFactory=NewsViewModelProviderFactory(newsRepository)
        viewModel= ViewModelProvider(this,newsViewModelProviderFactory)[NewsViewModel::class.java]

        binding.bottomNavView.setupWithNavController(fragmentContainerView.findNavController())
    }
}
