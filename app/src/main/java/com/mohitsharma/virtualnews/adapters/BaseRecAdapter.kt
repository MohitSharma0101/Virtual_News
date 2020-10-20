package com.mohitsharma.virtualnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharma.virtualnews.model.Article


abstract class BaseRecAdapter<T:Any>() : RecyclerView.Adapter<BaseRecAdapter.ViewHolder>() {
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
         LayoutInflater.from(parent.context)
             .inflate(inflateView(),parent,false)
     )

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    protected  val differ = AsyncListDiffer(returnInstance(), differCallback)


    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    abstract fun inflateView():Int
    abstract  fun returnInstance():BaseRecAdapter<T>
    override fun getItemCount(): Int = differ.currentList.size
 }