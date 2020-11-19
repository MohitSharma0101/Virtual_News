package com.mohitsharma.virtualnews.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import com.mohitsharma.virtualnews.util.*
import com.mohitsharma.virtualnews.util.Constants.SEARCH_DELAY_TIME
import kotlinx.android.synthetic.main.categories_layout.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SearchFragment : BaseFragment(R.layout.search_fragment) {

    lateinit var searchAdapter: RecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = RecyclerAdapter(viewModel)
        search_rec_view.setUpWithAdapter(requireContext(), searchAdapter)

        observeSearchNews()
        handleBackPress()
        observeCategoryNews()
        observeTopBar()

        btn_search.setOnClickListener {
            viewModel.searchTopBarState.postValue(TopBarState.SearchState())
        }


        search_bar.setOnSearchListener {
            MainScope().launch {
                delay(SEARCH_DELAY_TIME)
                it.let {
                    if (it.isNotEmpty()) {
                        viewModel.getSearchNews(it)
                    }
                }
            }
            viewModel.searchTopBarState.postValue(TopBarState.SearchState(it))
        }

        business_card.setOnClickListener {
            activateCategoryState(Constants.BUSINESS)
        }
        entertainment_card.setOnClickListener {
            activateCategoryState(Constants.ENTERTAINMENT)
        }
        health_card.setOnClickListener {
            activateCategoryState(Constants.HEALTH)
        }
        science_card.setOnClickListener {
            activateCategoryState(Constants.SCIENCE)
        }
        sports_card.setOnClickListener {
            activateCategoryState(Constants.SPORTS)
        }
        technology_card.setOnClickListener {
            activateCategoryState(Constants.TECHNOLOGY)
        }

        ib_clear_selection.setOnClickListener {
            viewModel.searchTopBarState.postValue(TopBarState.NormalState())
        }

    }

    private fun activateCategoryState(category: String) {
        viewModel.getNewsByCategory(category)
        viewModel.searchTopBarState.postValue(TopBarState.CategoryState(category.format()))
    }

    private fun observeTopBar() {
        viewModel.searchTopBarState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is TopBarState.SearchState -> {
                    ib_clear_selection.hide()

                    if (state.query != null) {
                        state.query.let {
                            search_bar.setSearchedPhrase(it)
                        }
                    }
                    search_top_bar.hide()
                    search_bar.show()
                    search_rec_view.show()
                }
                is TopBarState.CategoryState -> {
                    state.category.let {
                        ib_clear_selection.show()
                        top_bar_title.text = it
                        search_rec_view.show()
                    }
                }
                is TopBarState.NormalState -> {
                    ib_clear_selection.hide()
                    top_bar_title.text = Constants.CATEGORY
                    search_rec_view.hide()
                }
            }
        })
    }


    private fun observeSearchNews() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    it.data?.let { newsResponse ->
                        progress_bar.hide()
                        searchAdapter.savedDiffer.submitList(newsResponse.articles)
                    }
                }
                is Resources.Loading -> {
                    progress_bar.show()
                }
                is Resources.Error -> {
                    requireContext().toast("No Result Found!")
                }
                else -> {
                }
            }
        })
    }

    private fun observeCategoryNews() {
        viewModel.categoryNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resources.Success -> {
                    it.data?.let { newsResponse ->
                        searchAdapter.savedDiffer.submitList(newsResponse.articles)
                        progress_bar.hide()
                    }
                }
                is Resources.Loading -> {
                    progress_bar.show()
                }
                is Resources.Error -> {
                    progress_bar.hide()
                    requireContext().toast("No Result Found!")
                }
                else -> {
                }
            }
        })
    }

    fun showKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun handleBackPress() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when (viewModel.searchTopBarState.value) {
                is TopBarState.SearchState -> {
                    search_bar.clickBackButton()
                    search_bar.clearSearch()
                    search_bar.clearFocus()
                    search_bar.hide()
                    search_rec_view.hide()
                    search_top_bar.show()
                    top_bar_title.text = Constants.CATEGORY
                    viewModel.searchTopBarState.postValue(TopBarState.NormalState())
                }
                is TopBarState.CategoryState -> {
                    top_bar_title.text = Constants.CATEGORY
                    search_rec_view.hide()
                    viewModel.searchTopBarState.postValue(TopBarState.NormalState())
                }
                else -> {
                    viewModel.searchTopBarState.postValue(TopBarState.NormalState())
                    findNavController().popBackStack()
                }
            }
        }
    }

}