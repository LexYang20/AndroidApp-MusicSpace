package com.example.musicspace.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicspace.view.PlayPauseSkipActivity
import com.example.musicspace.R
import com.example.musicspace.adapter.SongAdapter
import com.example.musicspace.model.Song
import com.example.musicspace.model.SongModel
import com.example.musicspace.viewmodel.PlaylistViewModel
import com.example.musicspace.viewmodel.SearchViewModel
import com.google.android.material.navigation.NavigationView

class SearchActivity : AppCompatActivity() {
    private lateinit var currentLayout: DrawerLayout
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        currentLayout = findViewById(R.id.searchLayout)
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

        val searchView: SearchView = findViewById(R.id.searchView)

        val songs:List<Song> = getAvailableSongs()


        val recyclerViewSearch: RecyclerView = findViewById<RecyclerView>(R.id.recyclerViewSearch)
        val searchSongModelArrayList: ArrayList<SongModel> = ArrayList<SongModel>()

        val topSongAdapter = SongAdapter(this, searchSongModelArrayList)
        recyclerViewSearch.layoutManager = GridLayoutManager(this, 2)
        recyclerViewSearch.adapter = topSongAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchSongModelArrayList.clear()
                for(song in songs){
                    if(song.title.contains(query.toString())){
                        searchSongModelArrayList.add(SongModel(song.title, song.imageResId))
                    }
                }

                val queryAdapter = SongAdapter(this@SearchActivity, searchSongModelArrayList)
                recyclerViewSearch.adapter =  queryAdapter
                queryAdapter.setOnClickListener(object :
                    SongAdapter.OnClickListener {
                    override fun onClick(position: Int, model: SongModel) {
                        Log.i("SongAdapter", "position=" + position)

                        // Create an intent to navigate to CityInfoActivity
                        val playPauseSkipActivity = Intent(this@SearchActivity, PlayPauseSkipActivity::class.java)
                        // Pass relevant data about the selected city to CityInfoActivity
                        playPauseSkipActivity.putExtra(getString(R.string.song_index), position)
                        startActivity(playPauseSkipActivity)
                    }
                })
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchSongModelArrayList.clear()
                for(song in songs){
                    if(song.title.contains(newText.toString())){
                        searchSongModelArrayList.add(SongModel(song.title, song.imageResId))
                    }
                }
                val queryAdapter = SongAdapter(this@SearchActivity, searchSongModelArrayList)
                recyclerViewSearch.adapter =  queryAdapter
                queryAdapter.setOnClickListener(object :
                    SongAdapter.OnClickListener {
                    override fun onClick(position: Int, model: SongModel) {
                        Log.i("SongAdapter", "position=" + position)

                        // Create an intent to navigate to CityInfoActivity
                        val playPauseSkipActivity = Intent(this@SearchActivity, PlayPauseSkipActivity::class.java)
                        // Pass relevant data about the selected city to CityInfoActivity
                        playPauseSkipActivity.putExtra(getString(R.string.song_index), getPosition(model))
                        startActivity(playPauseSkipActivity)
                    }
                })
                return false
            }
        })

        topSongAdapter.setOnClickListener(object :
            SongAdapter.OnClickListener {
            override fun onClick(position: Int, model: SongModel) {
                Log.i("SongAdapter", "position=" + position)

                // Create an intent to navigate to CityInfoActivity
                val playPauseSkipActivity = Intent(this@SearchActivity, PlayPauseSkipActivity::class.java)
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

    private fun getPosition(song:SongModel):Int {
        val songs = getAvailableSongs()
        for(i in 0..5){
            if(songs[i].title==song.getSongName()){
                return i;
            }
        }
        return 0;
    }
}