package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.HomeRecAdapter
import com.mohitsharma.virtualnews.util.Resources
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : BaseFragment(R.layout.home_fragment) {

    lateinit var adapter:HomeRecAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         adapter = HomeRecAdapter()
        view_pager.adapter = adapter
        view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
        view_pager.currentItem = viewModel.currentNewsPosition
        val transformer = ViewPager2.PageTransformer { page, position ->
            page.apply {
                page.alpha = 0.5f + (1 - Math.abs(position))
            }
        }

        view_pager.setPageTransformer(transformer)
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    it.data?.let { newsResponse ->

                        adapter.differ.submitList(newsResponse.articles)
                    }
                }
                else -> {
                    Log.d("Response", "Error")
                }
            }
        })

    }







}