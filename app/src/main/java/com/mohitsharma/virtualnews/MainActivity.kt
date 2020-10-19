package com.mohitsharma.virtualnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.BottomBarItem
import me.ibrahimsn.lib.BottomBarParser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom,menu)
        bottomBar.setupWithNavController(menu!!,findNavController(R.id.nav_host_fragment))
        return true
    }
}