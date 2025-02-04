package com.example.musicspace.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicspace.view.PlayPauseSkipActivity
import com.example.musicspace.R
import com.example.musicspace.adapter.SongAdapter
import com.example.musicspace.model.Song
import com.example.musicspace.model.SongModel
import com.google.android.material.navigation.NavigationView

class LibraryActivity : AppCompatActivity() {
    private lateinit var currentLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        currentLayout = findViewById(R.id.libraryLayout)
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

        val recyclerViewTopPop: RecyclerView = findViewById(R.id.recyclerViewLibrary)
        val topPopSongModelArrayList: ArrayList<SongModel> = ArrayList()
        val songs :List<Song> = getAvailableSongs();
        for(song in songs) {
            topPopSongModelArrayList.add(SongModel(song.title, getDrawableIdForSong(song.id)))
        }

        val topSongAdapter = SongAdapter(this, topPopSongModelArrayList)
        recyclerViewTopPop.layoutManager = GridLayoutManager(this, 2)
        recyclerViewTopPop.adapter = topSongAdapter

        topSongAdapter.setOnClickListener(object :
            SongAdapter.OnClickListener {
            override fun onClick(position: Int, model: SongModel) {
                Log.i("SongAdapter", "position=$position")

                // Create an intent to navigate to CityInfoActivity
                val playPauseSkipActivity = Intent(this@LibraryActivity, PlayPauseSkipActivity::class.java)
                // Pass relevant data about the selected city to CityInfoActivity
                playPauseSkipActivity.putExtra(getString(R.string.song_index), position)
                startActivity(playPauseSkipActivity)
            }
        })
    }

    // Function to navigate to a specified activity class
    private fun navigateTo(activityClass: Class<*>) {
        // Check if the user has clicked a country button before navigating

        // Start the specified activity with the selected country as an extra
        val intent = Intent(this, activityClass).apply {

        }
        startActivity(intent)
    }

    private fun getAvailableSongs(): List<Song> {
        // Example of manually creating a list of songs
        return listOf(
            Song("song1", "GO Back", R.drawable.song1_img),
            Song("song2", "Chart Music", R.drawable.song2_img),
            Song("song3", "Haiya Ho lyrics", R.drawable.song3_img),
            Song("song4", "Birdcage", R.drawable.song4_img),
            Song("song5", "Rise My Love", R.drawable.song5_img),
            Song("song6", "I Know", R.drawable.song6_img),
        )
    }

    private fun getDrawableIdForSong(songId: String): Int {
        return when (songId) {
            "song1" -> R.drawable.song1_img
            "song2" -> R.drawable.song2_img
            "song3" -> R.drawable.song3_img
            "song4" -> R.drawable.song4_img
            "song5" -> R.drawable.song5_img
            "song6" -> R.drawable.song6_img
            else -> R.drawable.song  // A default image if no match is found
        }
    }
}