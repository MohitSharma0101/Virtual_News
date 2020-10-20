package com.mohitsharma.virtualnews.api

import com.mohitsharma.virtualnews.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
            @Query("country")
            countryCode:String = "india",
            @Query("page")
            pageCount:Int = 1,
            @Query("apikey")
            key: String = KEY.value
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
            @Query("q")
            query:String ,
            @Query("page")
            pageCount:Int = 1,
            @Query("apikey")
            key: String = KEY.value
    ):Response<NewsResponse>
}