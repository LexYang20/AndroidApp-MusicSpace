package com.example.musicspace.view

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musicspace.R
import com.example.musicspace.model.PlaySong
import com.example.musicspace.viewmodel.PlayPauseSkipViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlayPauseSkipActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayPauseSkipViewModel
    private lateinit var gestureDetector: GestureDetector
    private var isPlaying: Boolean = false
    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex = 0
    private val songUris = mutableListOf<Uri>()
    private lateinit var seekBar: SeekBar
    private lateinit var songTitle: TextView
    private lateinit var songArtist: TextView
    private lateinit var imgSongCover: ImageView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    private val handler = Handler()
    private lateinit var updateSeekBarRunnable: Runnable

    private lateinit var songDatabaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_pause_skip)

        initializeViews()
        initializeViewModel()
        setupButtonListeners()
        setupGestureDetector()

        currentSongIndex = intent.getIntExtra(getString(R.string.song_index), 0)
        songDatabaseReference = FirebaseDatabase.getInstance().getReference("songs")

        loadSongsFromDeviceStorage()
    }

    private fun initializeViews() {
        seekBar = findViewById(R.id.seekBar)
        songTitle = findViewById(R.id.songTitle)
        songArtist = findViewById(R.id.songArtist)
        imgSongCover = findViewById(R.id.imgSongCover)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrev)

        initSeekBarRunnable()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this).get(PlayPauseSkipViewModel::class.java)
        viewModel.currentSong.observe(this, { song ->
            updateUIWithSongDetails(song)
        })
    }

    private fun setupButtonListeners() {
        btnPlayPause.setOnClickListener { togglePlayPause() }
        btnNext.setOnClickListener { skipToNextSong() }
        btnPrevious.setOnClickListener { skipToPreviousSong() }
        imgSongCover.setOnClickListener { showSongDetails(currentSongIndex) }
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                val deltaX = e2.x - (e1?.x ?: 0f)
                if (Math.abs(deltaX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaX > 0) skipToPreviousSong() else skipToNextSong()
                    return true
                }
                return false
            }
        })
    }

    private fun loadSongsFromDeviceStorage() {
        songUris.clear()
        val resourceNames = arrayOf("song1", "song2", "song3", "song4", "song5", "song6")

        for (name in resourceNames) {
            val resId = resources.getIdentifier(name, "raw", packageName)
            if (resId != 0) {
                val uri = Uri.parse("android.resource://$packageName/$resId")
                songUris.add(uri)
            }
        }

        if (songUris.isNotEmpty()) {
            prepareMediaPlayer(currentSongIndex)
            updateSongDetailsFromDatabase(currentSongIndex)
        } else {
            Toast.makeText(this, "No songs available to play", Toast.LENGTH_LONG).show()
        }
    }

    private fun prepareMediaPlayer(index: Int) {
        mediaPlayer?.release()
        val uri = songUris.getOrNull(index)
        uri?.let {
            mediaPlayer = MediaPlayer.create(this, it)
            mediaPlayer?.apply {
                setOnPreparedListener { mp ->
                    mp.start()
                    this@PlayPauseSkipActivity.isPlaying = true
                    updatePlayPauseButton()
                    seekBar.max = mp.duration
                    startSeekBarUpdate()
                }
                setOnCompletionListener { skipToNextSong() }
            }
        } ?: Toast.makeText(this, "Unable to load song", Toast.LENGTH_LONG).show()
    }

    private fun initSeekBarRunnable() {
        updateSeekBarRunnable = Runnable {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    seekBar.progress = it.currentPosition
                    handler.postDelayed(updateSeekBarRunnable, 1000)
                }
            }
        }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (isPlaying) {
                it.pause()
                isPlaying = false
            } else {
                it.start()
                isPlaying = true
            }
            updatePlayPauseButton()
            if (isPlaying) startSeekBarUpdate() else stopSeekBarUpdate()
        }
    }

    private fun skipToNextSong() {
        if (songUris.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songUris.size
            prepareMediaPlayer(currentSongIndex)
            updateSongDetailsFromDatabase(currentSongIndex)
        }
    }

    private fun skipToPreviousSong() {
        if (songUris.isNotEmpty()) {
            currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songUris.size - 1
            prepareMediaPlayer(currentSongIndex)
            updateSongDetailsFromDatabase(currentSongIndex)
        }
    }


    private fun startSeekBarUpdate() {
        handler.post(updateSeekBarRunnable)
    }

    private fun stopSeekBarUpdate() {
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    private fun updatePlayPauseButton() {
        btnPlayPause.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
    }


    private fun updateUIWithSongDetails(song: PlaySong) {
        songTitle.text = song.title
        songArtist.text = song.artist

        val coverResName = "song${currentSongIndex + 1}"
        val coverResId = resources.getIdentifier(coverResName, "drawable", packageName)
        if (coverResId != 0) {
            imgSongCover.setImageResource(coverResId)
        } else {
            imgSongCover.setImageResource(R.drawable.song1)
        }
    }



    private fun updateSongDetailsFromDatabase(songIndex: Int) {
        val songNumber = songIndex + 1
        songDatabaseReference.child("song$songNumber").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val title = snapshot.child("title").getValue(String::class.java) ?: "Unknown"
                val artist = snapshot.child("artist").getValue(String::class.java) ?: "Unknown"
                val releaseYear = snapshot.child("releaseDate").getValue(Long::class.java) ?: -1L
                val lyrics = snapshot.child("lyrics").getValue(String::class.java) ?: "No lyrics available"


                viewModel.updateCurrentSong(PlaySong(title, artist, releaseYear, lyrics))
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PlayPauseSkipActivity, "Failed to load song details", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun showSongDetails(songIndex: Int) {
        val song = viewModel.currentSong.value ?: return
        val dialogView = layoutInflater.inflate(R.layout.dialog_song_detail, null)
        val titleTextView = dialogView.findViewById<TextView>(R.id.titleTextView)
        val artistTextView = dialogView.findViewById<TextView>(R.id.artistTextView)
        val releaseYearTextView = dialogView.findViewById<TextView>(R.id.releaseDateTextView)
        val lyricsButton = dialogView.findViewById<TextView>(R.id.lyricsTextView)

        titleTextView.text = song.title
        artistTextView.text = song.artist
        releaseYearTextView.text = song.releaseYear.toString()
        lyricsButton.setOnClickListener { showLyricsDialog(song.lyrics) }

        val exitButton = dialogView.findViewById<ImageButton>(R.id.exit)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        exitButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showLyricsDialog(lyrics: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_lyrics, null)
        val lyricsTextView = dialogView.findViewById<TextView>(R.id.lyricsTextView)
        lyricsTextView.text = lyrics

        val exitButton = dialogView.findViewById<ImageButton>(R.id.exitButton)
        val lyricsDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        exitButton.setOnClickListener { lyricsDialog.dismiss() }
        lyricsDialog.show()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        stopSeekBarUpdate()
    }

    companion object {
        const val SWIPE_THRESHOLD = 100
        const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}
