package com.example.musicspace.view

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicspace.R
import com.example.musicspace.adapter.PlaylistAdapter
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaylistActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(PlaylistActivity::class.java)

    @Test
    fun testAddSongButton() {
        onView(withId(R.id.addSongButton)).perform(click())
        onView(withText("GO Back")).check(matches(isDisplayed()))
        onView(withText("GO Back")).perform(click())
    }

    @Test
    fun testRemoveSongFromPlaylist() {
        // First add a song to ensure there is an item to remove
        onView(withId(R.id.addSongButton)).perform(click())
        onView(withText("GO Back")).perform(click())
        // Scroll to the song position in RecyclerView and click remove
        onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToPosition<PlaylistAdapter.ViewHolder>(0))
            .perform(RecyclerViewActions.actionOnItemAtPosition<PlaylistAdapter.ViewHolder>(0, clickChildViewWithId(R.id.removeButton)))
        // Check if the song is removed from the RecyclerView
        onView(withId(R.id.recyclerView)).check(matches(not(hasDescendant(withText("GO Back")))))
    }

    private fun clickChildViewWithId(id: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click on a child view with specified ID."
        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }

    @Test
    fun testSharePlaylistButton() {
        onView(withId(R.id.sharePlaylistButton)).perform(click())
    }

    @Test
    fun testRecyclerViewVisibility() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).perform(swipeUp())
    }

}