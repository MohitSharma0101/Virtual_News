package com.mohitsharma.virtualnews.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.adapters.RecyclerAdapter
import com.mohitsharma.virtualnews.util.setUpWithAdapter
import com.mohitsharma.virtualnews.util.toast
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.saved_fragment.*

class SavedFragment : BaseFragment(R.layout.saved_fragment) {
    lateinit var savedAdapter: RecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
//        viewModel.viewModelScope.launch(Dispatchers.Main) {
//            viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
//                savedAdapter.savedDiffer.submitList(it)
//            })
//        }
        viewModel.savedNewsLiveData.observe(viewLifecycleOwner, Observer {
            savedAdapter.savedDiffer.submitList(it)
            savedAdapter.notifyDataSetChanged()
        })


    }

    private fun setUpRecyclerView(){
        savedAdapter = RecyclerAdapter()
        saved_rec_view.setUpWithAdapter(requireContext(),savedAdapter)

        val simpleCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
            , ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currentArticle = savedAdapter.savedDiffer.currentList[position]
                viewModel.deleteArticle(currentArticle)
                context?.toast("Deleted")
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
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(saved_rec_view)
    }


}