package com.mohitsharma.virtualnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.model.Article
import kotlinx.android.synthetic.main.saved_article.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val savedDifferCallback: DiffUtil.ItemCallback<Article> = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.description == newItem.description

    }
    val savedDiffer = AsyncListDiffer(this, savedDifferCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.saved_article, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = savedDiffer.currentList[position]
        holder.itemView.apply {
            tv_headline.text = article.title
            tv_date.text = article.publishedAt
            Glide.with(this)
                    .load(article.urlToImage)
                    .centerCrop()
                    .signature(ObjectKey(article.urlToImage))
                    .into(iv_content_image)
        }
    }


    override fun getItemCount(): Int = savedDiffer.currentList.size
}