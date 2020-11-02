package com.mohitsharma.virtualnews.util

import android.content.Context
import android.widget.Toast
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse

fun Context.toast(msg:String){
   Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

 fun NewsResponse.filterResponse(): NewsResponse {
   val list = mutableListOf<Article>()
   for (article in this.articles) {
      if (article.description != "" && article.description != null) {
         article.apply {
            publishedAt = formatDate(publishedAt)
            author = formatAuthor(author)
         }
      } else {
         list.add(article)
      }
   }
   this.articles = this.articles.minus(list)
   return this
}

private fun formatAuthor(a: String): String {
   return if (a == "" || a == null) {
      "Source: Unknown"
   } else {
      "Source: $a"
   }
}

private fun formatDate(d: String): String {
   val year = d.substring(0, 4)
   val month = d.substring(5, 7)
   val date = d.substring(8, 10)
   return "published at: $date-$month-$year"
}