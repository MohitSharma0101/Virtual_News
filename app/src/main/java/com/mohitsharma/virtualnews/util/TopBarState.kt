package com.mohitsharma.virtualnews.util

import android.view.View
import com.mohitsharma.virtualnews.model.Article

sealed class TopBarState(
    val selectedItem:MutableList<Article>?=null
) {
    class NormalState() : TopBarState()
    class SelectionState(private var selectedItemList: MutableList<Article>) : TopBarState(selectedItemList)

}