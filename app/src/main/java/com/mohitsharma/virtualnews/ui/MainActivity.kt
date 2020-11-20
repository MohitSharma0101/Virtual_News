package com.mohitsharma.virtualnews.ui

import am.appwise.components.ni.NoInternetDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hbb20.countrypicker.models.CPCountry
import com.hbb20.countrypicker.view.prepareCustomCountryPickerView
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.database.ArticleDatabase
import com.mohitsharma.virtualnews.repository.DataStoreRepository
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.util.countryPicker.CustomCountryPicker
import com.mohitsharma.virtualnews.util.setOnItemReselectedListener
import com.mohitsharma.virtualnews.util.swipeDetector.SwipeActions
import com.mohitsharma.virtualnews.util.swipeDetector.SwipeGestureDetector
import com.mohitsharma.virtualnews.util.toast
import com.mukesh.countrypicker.CountryPicker
import com.mukesh.countrypicker.listeners.OnCountryPickerListener
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.country_picker_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var dataStoreRepository: DataStoreRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        
        dataStoreRepository = DataStoreRepository(this)
        dataStoreRepository.readUiModeFromDataStore.asLiveData()
            .observe(this, Observer { isDarkMode ->
                nightModeButton.isChecked = isDarkMode
            })


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        setUpBottomSheet()
        noInternetAlert()
        detectSwipeGestures()
//        dataStoreRepository.readCountryFromDataStore.asLiveData()

        val navController = findNavController(R.id.nav_host_fragment)
        ExpandableBottomBarNavigationUI.setupWithNavController(
            bottom_bar,
            navController
        )
        bottom_bar.setOnItemReselectedListener { view, menuItem ->
            viewModel.currentNewsPosition = 0
            navController.navigate(menuItem.itemId)
        }

    }

    private fun detectSwipeGestures(){
        val swipeGestureDetector = SwipeGestureDetector(object : SwipeActions {
            override fun onSwipeLeft() {}

            override fun onSwipeUp() {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })

        val gestureDetectorCompat = GestureDetectorCompat(applicationContext, swipeGestureDetector)
        btn_swipe_up.setOnTouchListener { view, motionEvent ->
            gestureDetectorCompat.onTouchEvent(motionEvent)
            view.performClick()
            true
        }
    }

    private fun restartActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun isDarkMode(isDarkMode: Boolean) = GlobalScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveUiMode(isDarkMode)
    }
    private fun saveCurrentCountry(country:String) =GlobalScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveToDataStore(country)
    }


    private fun setUpBottomSheet(){
        val countryPicker =   CustomCountryPicker(this)
        enable_dark_mode.setOnClickListener {
            nightModeButton.performClick()
        }

        nightModeButton.setOnCheckedChangeListener { _, isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isDarkMode(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isDarkMode(false)
            }
        }

        choose_country.setOnClickListener {
            countryPicker.show(country_picker_bottom_sheet)
            countryPicker.adapter.setOnCountrySelectedListener {
                tv_selectedCountry.text = it.name
               saveCurrentCountry(it.code)
                countryPicker.dismiss()
                restartActivity()
            }
        }

        feedback.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "mohitsharma.2cse23@jecrc.ac.in", null)
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            startActivity(Intent.createChooser(emailIntent, "Feedback"))
        }

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