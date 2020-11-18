package com.mohitsharma.virtualnews.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse
import com.thefinestartist.finestwebview.FinestWebView


fun Context.toast(msg: String){
   Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.revealWithAnimation(){

    val cx: Int = this.measuredWidth
    val cy: Int = this.measuredHeight
    val finalRadius: Int = Math.max(this.width, this.height)
    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius.toFloat())
    this.show()
    anim.start()

}
fun View.hideWithAnimation(){

    val cx: Int = this.measuredWidth
    val cy: Int = this.measuredHeight

    val initialRadius: Int = Math.max(this.width, this.height)

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, initialRadius.toFloat(), 0f)

    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            this@hideWithAnimation.hide()
        }
    })
    anim.start()

}

fun RecyclerView.setUpWithAdapter(context: Context, adapter: RecyclerAdapter){
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