package com.example.andoid.shoppingcart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.andoid.shoppingcart.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigation()

    }

    private fun bottomNavigation() {
        // Find bottomNav
        val bottomNavigationView = binding.bottonNavigationView
        // Find NavController
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // Set the Navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}