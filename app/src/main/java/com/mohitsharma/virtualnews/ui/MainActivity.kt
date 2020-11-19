package com.mohitsharma.virtualnews.ui

import am.appwise.components.ni.NoInternetDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.database.ArticleDatabase
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.util.swipeDetector.SwipeActions
import com.mohitsharma.virtualnews.util.swipeDetector.SwipeGestureDetector
import com.mohitsharma.virtualnews.util.toast
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {

      //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();
       bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)



      val noInternetDialog =  NoInternetDialog.Builder(this)
            .setCancelable(false)
            .setDialogRadius(20f)
            .build()

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)


        ExpandableBottomBarNavigationUI.setupWithNavController(
            bottom_bar,
            findNavController(R.id.nav_host_fragment)
        )

        val swipeGestureDetector = SwipeGestureDetector(object : SwipeActions {
            override fun onSwipeLeft() {

            }

            override fun onSwipeUp() {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

        })

        enable_dark_mode.setOnClickListener {
            applicationContext.toast("Dark Mode")
        }


        nightModeButton.setOnCheckedChangeListener { button, b ->
            if(b){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        }





        val gestureDetectorCompat = GestureDetectorCompat(applicationContext, swipeGestureDetector)
        btn_swipe_up.setOnTouchListener { view, motionEvent ->
            gestureDetectorCompat.onTouchEvent(motionEvent)
            view.performClick()
            true
        }


    }

    private fun restartActivity(){
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }

}