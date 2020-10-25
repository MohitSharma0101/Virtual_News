package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.HomeRecAdapter
import com.mohitsharma.virtualnews.model.Article
import com.mohitsharma.virtualnews.model.NewsResponse
import com.mohitsharma.virtualnews.model.Source
import com.mohitsharma.virtualnews.util.Resources
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeRecAdapter()
        view_pager.adapter = adapter
        view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resources.Success -> {
                    it.data?.let { newsResponse ->

                        adapter.differ.submitList(newsResponse.articles)
                    }
                }
                else -> {
                    Log.d("Response","Error")
                }
            }
        })
    }


}