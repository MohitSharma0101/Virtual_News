package com.mohitsharma.virtualnews.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Adapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse
import com.thefinestartist.finestwebview.FinestWebView


fun Context.toast(msg:String){
   Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

fun RecyclerView.setUpWithAdapter(context: Context,adapter: RecyclerAdapter){
    this.adapter = adapter
   this.layoutManager = LinearLayoutManager(context)
}

fun Context.showArticleInWebView(article: Article){
    FinestWebView.Builder(this)
        .toolbarScrollFlags(0)
        .titleDefault(article.source.name)
        .gradientDivider(true)
        .webViewSupportZoom(true)
        .statusBarColorRes(R.color.light_blue)
        .show(article.url)
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
   this.articles = this.articles.minus(list) as MutableList<Article>
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
   return "Published at : $date-$month-$year"
}