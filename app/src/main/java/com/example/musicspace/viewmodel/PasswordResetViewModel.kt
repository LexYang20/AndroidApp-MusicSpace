package com.example.musicspace.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class PasswordResetViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val resetEmailStatus = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun sendPasswordResetEmail(email: String) {
        isLoading.value = true
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    resetEmailStatus.value = true
                } else {
                    errorMessage.value = task.exception?.message ?: "Error sending reset email"
                }
            }
    }
}
