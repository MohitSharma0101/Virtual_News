package com.mohitsharma.virtualnews.adapters

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.model.Article
import kotlinx.android.synthetic.main.item_article.view.*

class HomeRecAdapter: BaseRecAdapter() {
    override fun inflateView() = R.layout.item_article

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
                    .into(iv_content_image)
        }
    }

    override fun returnInstance() = this
    override val differCallback: DiffUtil.ItemCallback<Article> = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem

    }

}