package com.mohitsharma.virtualnews.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.util.Resources
import com.mohitsharma.virtualnews.util.TopBarState
import com.mohitsharma.virtualnews.util.filterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    private var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    val savedTopBarState: MutableLiveData<TopBarState> = MutableLiveData()

  var  savedNewsLiveData: LiveData<List<Article>>
    var currentNewsPosition = 0

    init {
        getBreakingNews("in")
       savedNewsLiveData = getSavedNews()
        savedTopBarState.postValue(TopBarState.NormalState())
    }

    fun deleteAllArticle() = viewModelScope.launch {
        newsRepository.deleteAllArticles()
    }

    fun deleteSelected(list: MutableList<Article>){
        for(article in list){
            deleteArticle(article)
        }
    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.saveArticle(article)
    }
    fun deleteArticle(article: Article) = viewModelScope.launch {
       newsRepository.deleteAricle(article)
    }

    suspend fun isArticleSaved(article: Article): Boolean {
        var a: Article? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            a = newsRepository.isArticleSaved(article)
        }
        job.join()
        return a != null
    }

     fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun getSearchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resources.Loading())
        val response = newsRepository.searchNews(query, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
              val newsResponse = breakingNewsResponse ?: resultResponse
                return Resources.Success(newsResponse.filterResponse())
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.articles.isNotEmpty())
                    Resources.Success(resultResponse.filterResponse())
                else
                    Resources.Error(response.message())
            }
        }
        return Resources.Error(response.message())
    }

    private fun getSavedNews() = newsRepository.getSavedNews()


    }

