package com.example.musicspace.view

import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicspace.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testValidLogin() {
        onView(withId(R.id.editTextEmail)).perform(typeText("valid@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
    }


    @Test
    fun testInvalidEmailFormat() {
        onView(withId(R.id.editTextEmail)).perform(typeText("invalidemail"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun testEmptyEmailField() {
        onView(withId(R.id.editTextEmail)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())

    }

    @Test
    fun testEmptyPasswordField() {
        onView(withId(R.id.editTextEmail)).perform(typeText("valid@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun testInvalidPassword() {
        onView(withId(R.id.editTextEmail)).perform(typeText("valid@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("wrongPassword"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun testInputFieldResetAfterFailedLogin() {
        // Enter credentials and attempt to login
        onView(withId(R.id.editTextEmail)).perform(typeText("invalid@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("wrongPassword"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun testOrientationChangePreservesInput() {
        onView(withId(R.id.editTextEmail)).perform(typeText("test@example.com"), closeSoftKeyboard())

        // Change orientation
        activityRule.scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        // Check if the input is preserved
        onView(withId(R.id.editTextEmail)).check(matches(withText("test@example.com")))
    }

}
