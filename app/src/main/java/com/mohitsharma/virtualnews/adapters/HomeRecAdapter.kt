package com.mohitsharma.virtualnews.adapters

import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.model.Article

class HomeRecAdapter: BaseRecAdapter<Article>() {
    override fun inflateView() = R.layout.item_article

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
    }

    override fun returnInstance()  = this
}