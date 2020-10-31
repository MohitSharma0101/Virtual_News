package com.mohitsharma.virtualnews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.saved_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*

class SavedFragment : BaseFragment(R.layout.saved_fragment) {
    lateinit var savedAdapter: RecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedAdapter = RecyclerAdapter()
        saved_rec_view.adapter = savedAdapter
        saved_rec_view.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            savedAdapter.savedDiffer.submitList(it)
        })
    }



}