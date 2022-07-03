package com.example.newsapp2.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp2.NewsApplication
import com.example.newsapp2.models.Article
import com.example.newsapp2.models.NewsResponse
import com.example.newsapp2.repositories.NewsRepository
import com.example.newsapp2.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app:Application,private val repository: NewsRepository):AndroidViewModel(app) {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse:NewsResponse?=null

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch (Dispatchers.IO){
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery:String) = viewModelScope.launch(Dispatchers.IO) {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNews(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticles=breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNews(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                searchNewsPage++
//                if(searchNewsResponse==null){
//                    searchNewsResponse=resultResponse
//                }else{
//                    val oldArticles=searchNewsResponse?.articles
//                    val newArticles=resultResponse.articles
//                    oldArticles?.clear()
//                    oldArticles?.addAll(newArticles)
//                }
//                return Resource.Success(searchNewsResponse?:resultResponse)
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    //Room
    fun saveArticle(article:Article)= viewModelScope.launch (Dispatchers.IO){
        repository.upsert(article)
    }
    fun getSaveArticles()=repository.getSavedNews()
    fun deleteArticle(article: Article)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response=repository.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handleBreakingNews(response))
            }else{
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Error"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response=repository.getSearchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchNews(response))
            }else{
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Error"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork= connectivityManager.activeNetwork ?: return false
            val networkCapabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> true
                }
            }
        }
        return false
    }
}
