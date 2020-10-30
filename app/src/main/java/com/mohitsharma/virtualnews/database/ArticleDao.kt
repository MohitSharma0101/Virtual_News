package com.mohitsharma.virtualnews.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.mohitsharma.virtualnews.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE title= :q")
    suspend fun getArticleByTitle( q: String):Article

    @Delete
    suspend fun deleteArticle(article: Article)
}