package com.example.musicspace.view

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.musicspace.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {
    private lateinit var currentLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        currentLayout = findViewById(R.id.userProfileLayout)
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
                    Log.i("BrowseActivity", "click nav_browse")
                    navigateTo(BrowseActivity::class.java)
                    true
                }

                R.id.nav_playlist -> {
                    // Navigate to CitiesActivity
                    Log.i("BrowseActivity", "click nav_playlist")
                    navigateTo(PlaylistActivity::class.java)
                    true
                }

                R.id.nav_app_setting -> {
                    // Navigate to CitiesActivity
                    Log.i("BrowseActivity", "click nav_app_setting")
                    navigateTo(AppSettingsActivity::class.java)
                    true
                }

                R.id.nav_library -> {
                    Log.i("BrowseActivity", "click nav_library")
                    navigateTo(LibraryActivity::class.java)
                    true
                }

                R.id.nav_search -> {
                    Log.i("BrowseActivity", "click nav_search")
                    navigateTo(SearchActivity::class.java)
                    true
                }

                R.id.nav_user_profile -> {
                    Log.i("BrowseActivity", "click nav_user_profile")
                    navigateTo(UserProfileActivity::class.java)
                    true
                }

                else -> false
            }
        }
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        user?.let {
            FirebaseFirestore.getInstance().collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.i(TAG, "==DocumentSnapshot data: ${document.data}")
                        val name = document.data?.get("name")
                        val email =  document.data?.get("email")
                        val phoneNumber =  document.data?.get("phoneNumber")
                        var editTextName: TextView = findViewById(R.id.editTextName)
                        var editTextPhone: TextView = findViewById(R.id.editTextPhone)
                        var editTextEmail: TextView = findViewById(R.id.editTextEmail)

                        editTextName.setText("Name: $name")
                        editTextPhone.setText("Phone: $phoneNumber")
                        editTextEmail.setText("Email: $email")
                    } else {
                        Log.i(TAG, "==No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "get failed with ", exception)
                }
        }


        var btnUpdatePwd: Button = findViewById(R.id.btnUpdatePwd)
        btnUpdatePwd.setOnClickListener {
            navigateTo(PasswordResetActivity::class.java)
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