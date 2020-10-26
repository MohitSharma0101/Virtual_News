package com.mohitsharma.virtualnews.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val newsRepository: NewsRepository):ViewModel() {

    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
   var  currentNewsPosition = 0

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse.filterResponse())
            }
        }
        return Resources.Error(response.message())
    }

    private fun NewsResponse.filterResponse() : NewsResponse  {

        val list = mutableListOf<Article>()
        for(article in this.articles){
            if(article.description != "" && article.description !=null ){
                article.apply {
                    publishedAt = formatDate(publishedAt)
                    author = formatAuthor(author)
                }
            }else{
                list.add(article)
            }
        }
        this.articles = this.articles.minus(list)
        return this
    }

    private fun formatAuthor(a:String):String{
        return if(a == "" || a == null){
            "Source: Unknown"
        }else{
            "Source: $a"
        }
    }

    private fun formatDate(d:String) :String {
        val year = d.substring(0,4)
        val month =  d.substring(5,7)
        val date = d.substring(8,10)
        return "published at: $date-$month-$year"
    }
}