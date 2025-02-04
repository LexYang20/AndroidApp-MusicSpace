package com.example.musicspace.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicspace.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(SignupActivity::class.java)

    @Test
    fun testValidSignup() {
        onView(withId(R.id.editTextEmail)).perform(typeText("newuser@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText("New User"), closeSoftKeyboard())
        onView(withId(R.id.editTextPhone)).perform(typeText("1234567890"), closeSoftKeyboard())
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    @Test
    fun testInvalidEmailFormat() {
        onView(withId(R.id.editTextEmail)).perform(typeText("invalidemail"), closeSoftKeyboard())
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    @Test
    fun testPasswordMismatch() {
        onView(withId(R.id.editTextEmail)).perform(typeText("user@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("different"), closeSoftKeyboard())
        onView(withId(R.id.btnCreateAccount)).perform(click())

    }

    @Test
    fun testEmptyFields() {
        // Leave all fields empty and attempt to sign up
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    @Test
    fun testNavigateToLogin() {
        onView(withId(R.id.textGoToLogin)).perform(click())
    }
    @Test
    fun testInvalidPhoneNumberFormat() {
        onView(withId(R.id.editTextEmail)).perform(typeText("user@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText("User"), closeSoftKeyboard())
        onView(withId(R.id.editTextPhone)).perform(typeText("123"), closeSoftKeyboard()) // Invalid phone number
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }

    @Test
    fun testEmptyNameField() {
        onView(withId(R.id.editTextEmail)).perform(typeText("user@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText(""), closeSoftKeyboard()) // Empty name
        onView(withId(R.id.editTextPhone)).perform(typeText("1234567890"), closeSoftKeyboard())
        onView(withId(R.id.btnCreateAccount)).perform(click())
    }

}
