package com.mohitsharma.virtualnews.ui

import am.appwise.components.ni.NoInternetDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.database.ArticleDatabase
import com.mohitsharma.virtualnews.repository.DataStoreRepository
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.util.setOnItemReselectedListener
import com.mohitsharma.virtualnews.util.swipeDetector.SwipeActions
import com.mohitsharma.virtualnews.util.swipeDetector.SwipeGestureDetector
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>
    lateinit var dataStoreRepository:DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {


        dataStoreRepository = DataStoreRepository(this)
        dataStoreRepository.readUiModeFromDataStore.asLiveData().observe(this, Observer {isDarkMode ->
            nightModeButton.isChecked = isDarkMode
        })

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();
       bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

      val noInternetDialog =  noInternetAlert()

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)


        val navController = findNavController(R.id.nav_host_fragment)
        ExpandableBottomBarNavigationUI.setupWithNavController(
            bottom_bar,
            navController
        )
       bottom_bar.setOnItemReselectedListener{ view, menuItem ->
           viewModel.currentNewsPosition = 0
           navController.navigate(menuItem.itemId)
       }


        val swipeGestureDetector = SwipeGestureDetector(object : SwipeActions {
            override fun onSwipeLeft() {}

            override fun onSwipeUp() {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })
        enable_dark_mode.setOnClickListener {
           nightModeButton.performClick()
        }

        nightModeButton.setOnCheckedChangeListener { _, isDarkMode ->
            if(isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isDarkMode(true)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isDarkMode(false)
            }
        }
        feedback.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto","mohitsharma.2cse23@jecrc.ac.in",null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback")
            startActivity(Intent.createChooser(emailIntent,"Feedback"))
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

    private fun isDarkMode(isDarkMode:Boolean) = GlobalScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveUiMode(isDarkMode)
    }

    private fun noInternetAlert() = NoInternetDialog.Builder(this)
        .setCancelable(false)
        .setDialogRadius(50f)
        .setBgGradientCenter(resources.getColor(R.color.light_blue))
        .setBgGradientStart(resources.getColor(R.color.light_blue))
        .setBgGradientEnd(resources.getColor(R.color.light_blue))
        .setButtonColor(resources.getColor(R.color.white))
        .setButtonIconsColor(resources.getColor(R.color.light_blue))
        .setButtonTextColor(resources.getColor(R.color.black))
        .setWifiLoaderColor(resources.getColor(R.color.light_blue))
        .build()


}