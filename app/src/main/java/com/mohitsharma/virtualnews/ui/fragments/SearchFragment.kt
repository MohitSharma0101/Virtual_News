package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView.OnQueryChangeListener
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.HomeRecAdapter
import com.mohitsharma.virtualnews.util.Resources
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment(R.layout.search_fragment) {

    lateinit var searchAdapter:HomeRecAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchNews()
        var job:Job?  = null
        floating_search_view.setOnQueryChangeListener(OnQueryChangeListener { oldQuery, _ -> //get suggestions based on newQuery
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                oldQuery?.let {
                    if(it.isNotEmpty())  viewModel.getSearchNews(oldQuery)
                }
            }
            //pass them on to the search view

        })

      searchAdapter = HomeRecAdapter(requireContext(),viewModel)
        search_rec_view.adapter = searchAdapter
        search_rec_view.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun searchNews(query:String){


    }
    private fun observeSearchNews(){
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    it.data?.let { newsResponse ->
                        searchAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                else -> {
                    Log.d("Response", "Error")
                }
            }
        })
    }
}