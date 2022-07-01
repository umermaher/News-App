package com.example.newsapp2.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp2.models.Article
import com.example.newsapp2.models.NewsResponse
import com.example.newsapp2.repositories.NewsRepository
import com.example.newsapp2.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository):ViewModel() {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode:String) = viewModelScope.launch (Dispatchers.IO){
        breakingNews.postValue(Resource.Loading())
        val response=repository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handleBreakingNews(response))
    }

    fun searchNews(searchQuery:String) = viewModelScope.launch(Dispatchers.IO) {
        searchNews.postValue(Resource.Loading())
        val response=repository.getSearchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNews(response))
    }

    private fun handleBreakingNews(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNews(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article:Article)= viewModelScope.launch (Dispatchers.IO){
        repository.upsert(article)
    }
    fun getSaveArticles()=repository.getSavedNews()
    fun deleteArticle(article: Article)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(article)
    }
}
