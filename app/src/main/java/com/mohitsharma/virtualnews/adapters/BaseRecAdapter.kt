package com.mohitsharma.virtualnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharma.virtualnews.model.Article


abstract class BaseRecAdapter() : RecyclerView.Adapter<BaseRecAdapter.ViewHolder>() {
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
         LayoutInflater.from(parent.context)
             .inflate(inflateView(),parent,false)
     )




    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    abstract val differCallback: DiffUtil.ItemCallback<Article>
    abstract fun inflateView():Int
    abstract  fun returnInstance():BaseRecAdapter
   // override fun getItemCount(): Int = differ.currentList.size
 }