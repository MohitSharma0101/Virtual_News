package com.mohitsharma.virtualnews.util

import android.view.View
import com.mohitsharma.virtualnews.model.Article

sealed class TopBarState(
    val selectedItem:MutableList<Article>?=null
) {
    class NormalState() : TopBarState()
    class SearchState(val query: String? = null) : TopBarState()
    class CategoryState(val category: String? = null) : TopBarState()
    class SelectionState(private var selectedItemList: MutableList<Article>) : TopBarState(selectedItemList)

}