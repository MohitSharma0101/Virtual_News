package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView.OnQueryChangeListener
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.HomeRecAdapter
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import com.mohitsharma.virtualnews.ui.MainActivity
import com.mohitsharma.virtualnews.util.Constants.SEARCH_DELAY_TIME
import com.mohitsharma.virtualnews.util.Resources
import com.mohitsharma.virtualnews.util.setUpWithAdapter
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment(R.layout.search_fragment) {

    lateinit var searchAdapter: RecyclerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSearchNews()





        var job: Job? = null
//        floating_search_view.setOnQueryChangeListener(OnQueryChangeListener { oldQuery, _ -> //get suggestions based on newQuery
//            job?.cancel()
//            job = MainScope().launch {
//                delay(SEARCH_DELAY_TIME)
//                oldQuery?.let {
//                    if (it.isNotEmpty()) viewModel.getSearchNews(oldQuery)
//                }
//            }
//            //pass them on to the search view
//
//        })

        
        searchAdapter = RecyclerAdapter()
        search_rec_view.setUpWithAdapter(requireContext(),searchAdapter)

    }

    private fun observeSearchNews() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    it.data?.let { newsResponse ->
                        searchAdapter.savedDiffer.submitList(newsResponse.articles)
                    }
                }
                else -> {
                    Log.d("Response", "Error")
                }
            }
        })
    }
}