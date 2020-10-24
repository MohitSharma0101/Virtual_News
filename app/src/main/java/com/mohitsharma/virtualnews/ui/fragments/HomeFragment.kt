package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.HomeRecAdapter
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.Source
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = Article(1,"mohit" , "000","black clover new season out now it is avialable on animpahe.com",
                "12 oct 2020 | 4.00pm", Source("0","0") , "black clover new season out now","00"," \"https://s3.eu-central-1.amazonaws.com/s3.cointelegraph.com/uploads/2020-10/ccf7c41e-5d24-438e-854d-324db171a2ca.jpg"
        )
        val list = mutableListOf<Article>()
        list.add(article)
        val adapter = HomeRecAdapter()
        adapter.differ.submitList(list)
        view_pager.adapter = adapter
        view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

}