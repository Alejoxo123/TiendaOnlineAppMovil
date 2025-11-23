package com.example.tiendaonlineapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {

    protected lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun setContentView(layoutResID: Int) {
        val inflater = LayoutInflater.from(this)


        val fullView = inflater.inflate(R.layout.activity_base, null)


        val contentFrame = fullView.findViewById<android.widget.FrameLayout>(R.id.contentFrame)


        inflater.inflate(layoutResID, contentFrame, true)


        super.setContentView(fullView)


        drawerLayout = fullView.findViewById(R.id.drawerLayout)
        navigationView = fullView.findViewById(R.id.navigationView)

        setupToolbar()
        setupMenu()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(android.view.Gravity.START)
        return true
    }

    private fun setupMenu() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
                R.id.nav_products -> startActivity(Intent(this, ProductosActivity::class.java))
                R.id.nav_cart -> startActivity(Intent(this, CarritoActivity::class.java))
                R.id.nav_map -> startActivity(Intent(this, MapaActivity::class.java))

                R.id.nav_logout -> {
                    val prefs = getSharedPreferences("ShopPointPrefs", MODE_PRIVATE)
                    prefs.edit().remove("ID_CLIENTE").apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }

            drawerLayout.closeDrawers()
            true
        }
    }
}
