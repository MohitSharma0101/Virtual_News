package com.mohitsharma.virtualnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mohitsharma.virtualnews.R
import com.mohitsharma.virtualnews.database.ArticleDatabase
import com.mohitsharma.virtualnews.repository.NewsRepository
import com.mohitsharma.virtualnews.util.hide
import com.mohitsharma.virtualnews.util.show
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom, menu)
        bottomBar.setupWithNavController(menu!!, findNavController(R.id.nav_host_fragment))
        return true
    }

    fun hideBottomBar(){
        bottomBar.hide()
        timer.start()
    }
}