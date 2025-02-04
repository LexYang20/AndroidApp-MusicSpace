package com.example.musicspace.view

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.musicspace.view.PlayPauseSkipActivity
import com.example.musicspace.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayPauseSkipActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(PlayPauseSkipActivity::class.java)

    @Test
    fun testPlayPauseButtonClick() {
        onView(withId(R.id.btnPlayPause)).perform(click())
    }

    @Test
    fun testNextButtonClick() {
        onView(withId(R.id.btnNext)).perform(click())
    }

    @Test
    fun testPreviousButtonClick() {
        onView(withId(R.id.btnPrev)).perform(click())
    }

    @Test
    fun testSongCoverClick() {
        onView(withId(R.id.imgSongCover)).perform(click())
    }
}
