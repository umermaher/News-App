package com.example.newsapp2.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import com.example.newsapp2.db.ArticleRoomDatabase
import com.example.newsapp2.repositories.NewsRepository
import com.example.newsapp2.viewmodels.NewsViewModel
import com.example.newsapp2.viewmodels.NewsViewModelProviderFactory

fun getNewsViewModel(context: Context,owner:Fragment):NewsViewModel{
    val newsRepository= NewsRepository(ArticleRoomDatabase(context))
    val newsViewModelProviderFactory= NewsViewModelProviderFactory(newsRepository)
    return ViewModelProvider(owner,newsViewModelProviderFactory)[NewsViewModel::class.java]
}