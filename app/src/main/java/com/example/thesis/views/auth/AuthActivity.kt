package com.example.thesis.views.auth

import com.example.thesis.views.loggedin.ViewPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.thesis.R

class AuthActivity : AppCompatActivity(){

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        navController = Navigation.findNavController(
            this@AuthActivity,
            R.id.nav_host_fragment
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        ).navigateUp() || super.onSupportNavigateUp()
    }

}