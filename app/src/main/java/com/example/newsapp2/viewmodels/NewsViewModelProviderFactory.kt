package com.example.newsapp2.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp2.repositories.NewsRepository

class NewsViewModelProviderFactory(val app:Application,private val newsRepository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = NewsViewModel(app,newsRepository) as T
}