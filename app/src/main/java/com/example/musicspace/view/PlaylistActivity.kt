package com.example.musicspace.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicspace.R
import com.example.musicspace.model.Song
import com.example.musicspace.model.Utility
import com.example.musicspace.adapter.PlaylistAdapter
import com.example.musicspace.viewmodel.PlaylistViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistActivity : AppCompatActivity() {

    private lateinit var currentLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistAdapter
    private val db = FirebaseFirestore.getInstance()
    private var playlist: MutableList<Song> = mutableListOf()
    private var currentPlaylistId: String = UUID.randomUUID().toString() // Unique ID for the playlist

    private lateinit var viewModel: PlaylistViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupAdapter(playlist)
        setupObservers()

        val addSongButton: Button = findViewById(R.id.addSongButton)
        addSongButton.setOnClickListener {
            showSongSelectionDialog()
        }

        val savePlaylistButton: Button = findViewById(R.id.savePlaylistButton)
        savePlaylistButton.setOnClickListener {
            savePlaylistToFirestore()
        }

        val sharePlaylistButton: Button = findViewById(R.id.sharePlaylistButton)
        sharePlaylistButton.setOnClickListener {
            sharePlaylist()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        currentLayout = findViewById(R.id.playlistLayout)
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

    private fun getAvailableSongs(): List<Song> {
        // manually creating a list of songs
        return listOf(
            Song("song1", "GO Back", R.drawable.song1_img),
            Song("song2", "Chart Music", R.drawable.song2_img),
            Song("song3", "Haiya Ho lyrics", R.drawable.song3_img),
            Song("song4", "Birdcage", R.drawable.song4_img),
            Song("song5", "Rise My Love", R.drawable.song5_img),
            Song("song6", "I Know", R.drawable.song6_img),
        )
    }

    private fun showSongSelectionDialog() {
        val songs = getAvailableSongs()
        val songTitles = songs.map { it.title }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Select a Song")
            .setItems(songTitles) { dialog, which ->
                val selectedSong = songs[which]
                viewModel.addSongToPlaylist(selectedSong.id, selectedSong.title)
            }
            .show()
    }

    private fun setupAdapter(songs: MutableList<Song>) {
        adapter = PlaylistAdapter(songs) { songId ->
            viewModel.removeSongFromPlaylist(songId)
        }
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.playlist.observe(this, { updatedPlaylist ->
            adapter.updatePlaylist(updatedPlaylist)
        })

        viewModel.saveSuccess.observe(this, { success ->
            if (success) {
                Toast.makeText(this, "Playlist saved successfully", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.saveError.observe(this, { error ->
            error?.let {
                Toast.makeText(this, "Error saving playlist: $it", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun savePlaylistToFirestore() {
        viewModel.savePlaylistToFirestore()
    }

    private fun sharePlaylist() {
        val qrCodeBitmap = Utility.generateQRCode(currentPlaylistId)
        if (qrCodeBitmap != null) {
            shareQRCode(qrCodeBitmap)
        } else {
            // Handle QR code generation failure
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareQRCode(bitmap: Bitmap) {
        // Save the bitmap to a file
        val cachePath = File(externalCacheDir, "my_images/")
        cachePath.mkdirs()
        val filePath = "$cachePath/playlist_qr.png"
        FileOutputStream(filePath).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }

        // Create a content URI for the file
        val imagePath = File(filePath)
        val contentUri = FileProvider.getUriForFile(
            this,
            "com.example.musicspace.fileprovider",
            imagePath
        )

        // Create and launch a sharing intent
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = contentResolver.getType(contentUri)
        }
        startActivity(Intent.createChooser(intent, "Share Playlist QR Code"))
    }
}
