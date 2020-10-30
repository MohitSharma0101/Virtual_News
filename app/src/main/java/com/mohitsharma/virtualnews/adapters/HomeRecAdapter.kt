package com.mohitsharma.virtualnews.adapters

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.ui.NewsViewModel
import kotlinx.android.synthetic.main.item_article.view.*

class HomeRecAdapter(var context:Context,var viewModel: NewsViewModel):RecyclerView.Adapter<HomeRecAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    @SuppressLint("ShowToast")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            tv_author.text = article.author
            tv_date.text = article.publishedAt
            tv_headline.text = article.title
            tv_content.text = article.description

            Glide.with(this)
                    .load(article.urlToImage)
                    .centerCrop()
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                    .into(iv_content_image)
            fab_saved.setOnClickListener {

                if(!viewModel.isArticleSaved(article)){
                    Log.d("Article"," Saved")
                    fab_saved.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                    Toast.makeText(context,"deleted",Toast.LENGTH_SHORT)
                    notifyDataSetChanged()
                }else{
                    fab_saved.setImageResource(R.drawable.ic_baseline_bookmarks_24)
                    article.isSaved = true
                    viewModel.saveArticle(article)
                    Toast.makeText(context,"Saved",Toast.LENGTH_SHORT)
                    notifyDataSetChanged()
                }

            }
        }
    }

     private val differCallback: DiffUtil.ItemCallback<Article> = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        )


    override fun getItemCount(): Int = differ.currentList.size


}