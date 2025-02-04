package com.example.musicspace.view
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicspace.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.After
import org.hamcrest.Matchers.not


@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(SearchActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testPresenceOfUIElements() {
        // Check if searchView is displayed
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))

        // Check if recyclerViewSearch is displayed
        onView(withId(R.id.recyclerViewSearch)).check(matches(isDisplayed()))

    }

    @Test
    fun testSearchView() {
        // Click the searchView
        onView(withId(R.id.searchView)).perform(click())
    }

}
