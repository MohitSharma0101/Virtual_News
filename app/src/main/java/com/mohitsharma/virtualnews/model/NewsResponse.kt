package com.mohitsharma.virtualnews.model

data class NewsResponse(
        var articles: List<Article>,
        val status: String,
        val totalResults: Int
)