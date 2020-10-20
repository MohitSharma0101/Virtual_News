package com.mohitsharma.virtualnews.adapters

import androidx.recyclerview.widget.DiffUtil
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.model.Article

class HomeRecAdapter: BaseRecAdapter() {
    override fun inflateView() = R.layout.item_article

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
    }

    override fun returnInstance() = this
    override val differCallback: DiffUtil.ItemCallback<Article> = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean = oldItem == newItem

    }

}