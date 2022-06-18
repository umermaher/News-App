package com.example.newsapp2.models

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)