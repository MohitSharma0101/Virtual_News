package com.mohitsharma.virtualnews.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.SavedRecAdapter
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse
import com.thefinestartist.finestwebview.FinestWebView
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.ExpandableBottomBarMenuItem


fun Context.toast(msg: String){
   Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}
fun String.format() :String {
    return "${this[0].toUpperCase()}${this.substring(1,this.length)}"
}

fun Float.convertToDp(context: Context): Float {
    return this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
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

fun RecyclerView.setUpWithAdapter(context: Context, adapter: SavedRecAdapter){
    this.adapter = adapter
   this.layoutManager = LinearLayoutManager(context)
}

fun ExpandableBottomBar.setOnItemReselectedListener(listener:((View, ExpandableBottomBarMenuItem) -> Unit)?) {
    this.onItemReselectedListener = listener
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
                     if(ifNotFormatted(this)){
                         publishedAt = formatDate(publishedAt)
                         author = formatAuthor(author)
                     }
                 }
             } else {
                 list.add(article)
             }
         }
         this.articles = this.articles.minus(list) as MutableList<Article>

         return this

}

private fun ifNotFormatted(article:Article):Boolean{
    article.author.apply {
        if(this != null){
            if( this.isNotEmpty() && this.length > 6){
                val source = this.substring(0..5)
                return source != "Source"
            }
        }
    }
    return true
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