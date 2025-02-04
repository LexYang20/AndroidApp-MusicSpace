package com.example.musicspace.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class SignupViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val signupState = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun createAccount(email: String, password: String, name: String, phoneNumber: String) {
        isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the user ID of the newly created user
                    val userId = auth.currentUser?.uid ?: ""
                    // Create a user object or a map to store in Firestore
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "phoneNumber" to phoneNumber,
                        "password" to password
                    )
                    // Add a new document with the user ID in Firestore
                    FirebaseFirestore.getInstance().collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            signupState.value = true
                        }
                        .addOnFailureListener { e ->
                            errorMessage.value = e.message
                        }
                } else {
                    val message = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "An account already exists for this email."
                        else -> "Authentication failed: ${task.exception?.message}"
                    }
                    errorMessage.value = message
                }
                isLoading.value = false
            }
    }
}
