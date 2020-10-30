package com.mohitsharma.virtualnews.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
        tableName = "articles"
)
data class Article(
        @PrimaryKey(autoGenerate = true)
        var id : Int? = null,
        var isSaved: Boolean? = false,
        var author: String,
        val content: String,
        val description: String,
        var publishedAt: String,
        val source: Source,
        val title: String,
        val url: String,
        val urlToImage: String
)