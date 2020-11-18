package com.mohitsharma.virtualnews.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.eldersoss.elderssearchview.EldersSearchView
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import com.mohitsharma.virtualnews.ui.MainActivity
import com.mohitsharma.virtualnews.util.*
import com.mohitsharma.virtualnews.util.Constants.SEARCH_DELAY_TIME
import kotlinx.android.synthetic.main.activity_main.*
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

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            search_bar.clickBackButton()
            search_bar.clearSearch()
            search_bar.clearFocus()
            search_bar.hide()
            search_rec_view.hide()
            search_top_bar.show()
        }

        btn_search.setOnClickListener {
            search_top_bar.hide()
            search_bar.revealWithAnimation()
            search_rec_view.show()
        }

        var job: Job? = null
        search_bar.setOnSearchTextChangeListener {
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY_TIME)
                it?.let {
                    if (it.isNotEmpty()) {
                        viewModel.getSearchNews(it.toString())

                    }
                }
            }
        }

        searchAdapter = RecyclerAdapter(viewModel)
        search_rec_view.setUpWithAdapter(requireContext(),searchAdapter)

    }


    private fun observeSearchNews() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    it.data?.let { newsResponse ->
                        Log.d("response",newsResponse.articles.isEmpty().toString())
                        searchAdapter.savedDiffer.submitList(newsResponse.articles)
                    }
                }
                is Resources.Loading -> {
                    Log.d("response","Loading")
                }
                is Resources.Error -> {
                    requireContext().toast("No Result Found!")
                }

                else -> { }
            }
        })
    }
    fun showKeyboard() {
        val inputMethodManager: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}