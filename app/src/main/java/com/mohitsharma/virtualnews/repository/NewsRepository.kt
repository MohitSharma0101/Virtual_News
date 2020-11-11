package com.mohitsharma.virtualnews.repository

import com.mohitsharma.virtualnews.api.RetrofitInstance
import com.mohitsharma.virtualnews.database.ArticleDatabase
import com.mohitsharma.virtualnews.model.Article

class NewsRepository (private val db:ArticleDatabase){

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber:Int) =
            RetrofitInstance.api.searchNews(searchQuery,pageNumber)

    suspend fun saveArticle(article:Article) = db.getArticleDao().save(article)
    suspend fun deleteAricle(article:Article) = db.getArticleDao().deleteArticle(article)

    suspend fun isArticleSaved(article: Article) = db.getArticleDao().getArticleByTitle(article.title)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteAllArticles() = db.getArticleDao().deleteAllArticle()

}