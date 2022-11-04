package com.example.thesis.views.loggedin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.thesis.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class ManagerActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        val navController: NavController =
            Navigation.findNavController(this, R.id.activity_manager_nav_host_fragment)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(com.example.thesis.R.id.bottomNavigationView)
        bottomNavigationView.getOrCreateBadge(R.id.friendsFragment2).number=2
        setupWithNavController(bottomNavigationView, navController)

    }


}