package com.example.musicspace.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicspace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Use private MutableLiveData for internal modification
    private val _loginSuccess = MutableLiveData<Boolean>()
    // Expose immutable LiveData for observation
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signIn(email: String, password: String) {
        _isLoading.value = true // Loading starts

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _isLoading.postValue(false)

            if (task.isSuccessful) {

                _loginSuccess.postValue(true)
            } else {
                // Construct error message based on the exception
                val message = when (task.exception) {
                    is FirebaseAuthInvalidUserException -> "User does not exist."
                    is FirebaseAuthInvalidCredentialsException -> "Invalid credentials."
                    else -> "Authentication failed: ${task.exception?.message}"
                }
                _loginError.postValue(message) // Login error
            }
        }
    }

    fun saveUserInfo() {

    }
}
