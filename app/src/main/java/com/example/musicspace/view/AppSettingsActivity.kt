package com.example.musicspace.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.musicspace.BluetoothActivity
import com.example.musicspace.R
import com.google.android.material.navigation.NavigationView

class AppSettingsActivity : AppCompatActivity() {
    private lateinit var currentLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_settings)


        var btnOfflineMode: Button =findViewById(R.id.btnOfflineMode)
        btnOfflineMode.setOnClickListener{
            Log.i("app settings","click OfflineMode")
        }

        var btnUIThemes: Button =findViewById(R.id.btnUIThemes)
        btnUIThemes.setOnClickListener{
            Log.i("app settings","click UIThemes")
        }


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        currentLayout = findViewById(R.id.appSettingLayout)
        val toggle = ActionBarDrawerToggle(
            this, currentLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        currentLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Set item click listener for navigation menu items
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_browse -> {
                    // Navigate to ProvinceInfoActivity
                    Log.i("BrowseActivity","click nav_browse")
                    navigateTo(BrowseActivity::class.java)
                    true
                }
                R.id.nav_playlist -> {
                    // Navigate to CitiesActivity
                    Log.i("BrowseActivity","click nav_playlist")
                    navigateTo(PlaylistActivity::class.java)
                    true
                }
                R.id.nav_app_setting -> {
                    // Navigate to CitiesActivity
                    Log.i("BrowseActivity","click nav_app_setting")
                    navigateTo(AppSettingsActivity::class.java)
                    true
                }
                R.id.nav_library -> {
                    Log.i("BrowseActivity","click nav_library")
                    navigateTo(LibraryActivity::class.java)
                    true
                }
                R.id.nav_search -> {
                    Log.i("BrowseActivity","click nav_search")
                    navigateTo(SearchActivity::class.java)
                    true
                }
                R.id.nav_user_profile -> {
                    Log.i("BrowseActivity","click nav_user_profile")
                    navigateTo(UserProfileActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }

    // Function to navigate to a specified activity class
    private fun navigateTo(activityClass: Class<*>) {
        // Check if the user has clicked a country button before navigating

        // Start the specified activity with the selected country as an extra
        val intent = Intent(this, activityClass).apply {

        }
        startActivity(intent)
    }
}