package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.util.DepthPageTransformer
import com.mohitsharma.virtualnews.util.Resources
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : BaseFragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpVIewPager()
        observeBreakingNews()

    }


    private fun observeBreakingNews() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Loading -> {
                    loading_view.visibility = View.VISIBLE
                }
                is Resources.Success -> {
                    loading_view.visibility = View.GONE
                    it.data?.let { newsResponse ->
                        adapter.differ.submitList(newsResponse.articles)
                    }
                }
                else -> {
                    loading_view.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setUpVIewPager() {
        view_pager.adapter = adapter
        view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
        view_pager.currentItem = viewModel.currentNewsPosition
        view_pager.setPageTransformer(DepthPageTransformer())
        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                val currentItem:Int = view_pager.currentItem + 1
                val lastItem:Int = adapter.itemCount
                Log.d("current" , currentItem.toString())
                Log.d("last" , lastItem.toString())

                if (currentItem == lastItem ){
                    viewModel.getBreakingNews("in")
                    Log.d("Last item " , "reached")
                }

                super.onPageSelected(position)
            }
        })
    }


}