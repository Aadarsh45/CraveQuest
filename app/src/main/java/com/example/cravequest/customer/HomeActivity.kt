package com.example.cravequest.customer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cravequest.R
import com.example.cravequest.customer.fragment.FavoriteFragment
import com.example.cravequest.customer.fragment.HomeFragment
import com.example.cravequest.customer.fragment.OrderHistoryFragment
import com.example.cravequest.customer.fragment.ProfileFragment
import com.example.cravequest.customer.fragment.scanWithAI
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeFragment = HomeFragment()
        val favoriteFragment = FavoriteFragment()
        val orderHistoryFragment = OrderHistoryFragment()
        val scanWithAIFragment = scanWithAI()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView) // Initialize correctly

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.favorite -> setCurrentFragment(favoriteFragment)
                R.id.order_history -> setCurrentFragment(orderHistoryFragment)
                R.id.scan_with_ai -> setCurrentFragment(scanWithAIFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}