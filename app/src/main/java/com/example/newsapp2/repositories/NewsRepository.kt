package com.example.newsapp2.repositories

import com.example.newsapp2.RetrofitInstance
import com.example.newsapp2.db.ArticleRoomDatabase
import com.example.newsapp2.models.Article

class NewsRepository(private val db:ArticleRoomDatabase) {
    suspend fun getBreakingNews(countryCode:String,pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun getSearchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article:Article) = db.getArticleDao().upsert(article)

    fun getSavedNews()= db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article)=db.getArticleDao().delete(article)
}