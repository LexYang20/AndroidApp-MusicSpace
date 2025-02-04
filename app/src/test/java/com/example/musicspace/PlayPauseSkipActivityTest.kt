package com.example.musicspace

import android.media.MediaPlayer
import android.net.Uri
import androidx.core.content.ContextCompat
import com.example.test.TestApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class, sdk = [25], manifest = Config.NONE)
class PlayPauseSkipActivityTest {

    private lateinit var activity: PlayPauseSkipActivity
    private lateinit var mediaPlayer: MediaPlayer

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(PlayPauseSkipActivity::class.java)
            .create()
            .resume()
            .get()

        mediaPlayer = mock(MediaPlayer::class.java)
        activity.mediaPlayer = mediaPlayer


        val packageName = "com.example.musicspace"
        activity.songUris.addAll(listOf(
            Uri.parse("android.resource://$packageName/${R.raw.song1}"),
            Uri.parse("android.resource://$packageName/${R.raw.song2}"),
            Uri.parse("android.resource://$packageName/${R.raw.song3}"),
            Uri.parse("android.resource://$packageName/${R.raw.song4}"),
            Uri.parse("android.resource://$packageName/${R.raw.song5}"),
            Uri.parse("android.resource://$packageName/${R.raw.song6}")
        ))
    }

  /*  @Test
    fun testPlaySong() {
        `when`(mediaPlayer.isPlaying).thenReturn(false, true)
        activity.prepareMediaPlayer(0)
        activity.togglePlayPause()

        ShadowLooper.idleMainLooper()

        verify(mediaPlayer).start()
        assert(activity.mediaPlayer?.isPlaying == true)
    }
*/
    @Test
    fun testPauseSong() {
        `when`(mediaPlayer.isPlaying).thenReturn(true, false)
        activity.prepareMediaPlayer(0)
        activity.togglePlayPause() // Start playing
        activity.togglePlayPause() // Pause

        ShadowLooper.idleMainLooper()

        verify(mediaPlayer).pause()
        assert(activity.mediaPlayer?.isPlaying == false)
    }

    @Test
    fun testSkipToNextSong() {
        `when`(mediaPlayer.isPlaying).thenReturn(true)
        activity.prepareMediaPlayer(0)
        activity.skipToNextSong()

        ShadowLooper.idleMainLooper()

        assert(activity.currentSongIndex == 1)
    }

  /*  @Test
    fun testSkipToPreviousSong() {
        `when`(mediaPlayer.isPlaying).thenReturn(true)
        activity.prepareMediaPlayer(1)
        activity.skipToPreviousSong()

        ShadowLooper.idleMainLooper()

        assert(activity.currentSongIndex == 0)
    }*/
}
