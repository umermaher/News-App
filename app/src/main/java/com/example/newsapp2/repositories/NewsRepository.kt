package com.example.newsapp2.repositories

import com.example.newsapp2.RetrofitInstance
import com.example.newsapp2.db.ArticleRoomDatabase

class NewsRepository(val db:ArticleRoomDatabase) {
    suspend fun getBreakingNews(countryCode:String,pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun getSearchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
}