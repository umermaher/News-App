package com.example.newsapp2

import com.example.newsapp2.utils.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode:String="us",
        @Query("page") pageNumber:Int=1,
        @Query("apiKey") apiKey:String=API_KEY
    )
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery:String,
        @Query("page") pageNumber:Int=1,
        @Query("apiKey") apiKey:String=API_KEY
    )
}