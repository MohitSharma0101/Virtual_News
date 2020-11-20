package com.mohitsharma.virtualnews.ui.fragments

import android.content.res.Resources
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.SavedRecAdapter
import com.mohitsharma.virtualnews.util.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.saved_fragment.*


class SavedFragment : BaseFragment(R.layout.saved_fragment) {
    lateinit var savedAdapter: SavedRecAdapter
    lateinit var itemTouchHelper:ItemTouchHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeTopBarState()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when(viewModel.savedTopBarState.value){
                is TopBarState.SelectionState ->clearSelection()
                else -> findNavController().popBackStack()
            }
        }

        viewModel.savedNewsLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()){
                iv_no_saved_news.show()
            } else {
                iv_no_saved_news.hide()
            }
            savedAdapter.savedDiffer.submitList(it)
            savedAdapter.notifyDataSetChanged()
        })

        btn_delete_all.setOnClickListener {
            confirmDeleteAlert()
        }
    }

    private fun observeTopBarState(){
        viewModel.savedTopBarState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TopBarState.SelectionState -> {
                    itemTouchHelper.attachToRecyclerView(null)
                    saved_top_bar.hide()
                    saved_selection_top_bar.show()
                    ib_clear_selection.setOnClickListener {
                        clearSelection()
                    }
                    btn_delete_selected.setOnClickListener {
                        viewModel.deleteSelected(savedAdapter.selectedItems)
                        clearSelection()
                    }
                    tv_item_count.text = savedAdapter.selectedItems.size.toString()
                }

                else -> {
                    itemTouchHelper.attachToRecyclerView(saved_rec_view)
                    saved_top_bar.show()
                    saved_selection_top_bar.hide()
                }
            }
        })
    }

    private  fun clearSelection(){
        viewModel.savedTopBarState.postValue(TopBarState.NormalState())
        savedAdapter.selectedItems.clear()
        findNavController().navigate(R.id.savedFragment)
    }

    private fun confirmDeleteAlert(){
        val alertDialog = MaterialAlertDialogBuilder(requireContext(),R.style.CustomMaterialDialog)
            .setIcon(R.drawable.ic_trash_2)
            .setTitle("Delete All Articles")
            .setMessage("Are you sure You want to delete All saved news?")
            .setPositiveButton("Yes") {_,_ ->
                viewModel.deleteAllArticle()

            }
            .setNegativeButton("No") { _,_ ->
            }
            .show()
    }



    private fun setUpRecyclerView(){
        savedAdapter = SavedRecAdapter(viewModel)
        saved_rec_view.setUpWithAdapter(requireContext(), savedAdapter)
//        savedAdapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
//            override fun onChanged() {
//                super.onChanged()
//                if(adapter.differ.currentList.isEmpty()){
//                    iv_no_saved_news.show()
//                }else{
//                    iv_no_saved_news.hide()
//                }
//            }
//        })

        val simpleCallBack = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currentArticle = savedAdapter.savedDiffer.currentList[position]
                viewModel.deleteArticle(currentArticle)
                view?.let { Snackbar.make(it,"Deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO"){
                        viewModel.saveArticle(currentArticle)
                        savedAdapter.notifyDataSetChanged()
                    }
                    show()
                } }
                savedAdapter.notifyDataSetChanged()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                ).addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.red_400)
                )
                    .addSwipeLeftActionIcon(R.drawable.ic_trash_2_white)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }
         itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(saved_rec_view)
    }




}