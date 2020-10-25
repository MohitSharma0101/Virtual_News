package com.mohitsharma.virtualnews.repository

import com.mohitsharma.virtualnews.api.RetrofitInstance
import com.mohitsharma.virtualnews.database.ArticleDatabase

class NewsRepository ( val db:ArticleDatabase){

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
}