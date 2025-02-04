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
class PasswordResetActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(PasswordResetActivity::class.java)

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
        // Check if email input field is displayed
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()))

        // Check if send button is displayed
        onView(withId(R.id.SendButton)).check(matches(isDisplayed()))

        // Check if progress bar is initially hidden
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testValidEmailInputWithFailureResponse() {
        onView(withId(R.id.editTextEmail)).perform(typeText("user@example.com"), closeSoftKeyboard())
        onView(withId(R.id.SendButton)).perform(click())
    }

    @Test
    fun testInvalidEmailInputWithFailureResponse() {
        onView(withId(R.id.editTextEmail)).perform(typeText("Invalid Email"), closeSoftKeyboard())
        onView(withId(R.id.SendButton)).perform(click())
    }

    @Test
    fun testEmptyEmailFieldValidation() {
        // Click the send button with an empty email
        onView(withId(R.id.SendButton)).perform(click())
    }

}
