package com.example.musicspace.view

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musicspace.R
import com.example.musicspace.databinding.ActivitySignupBinding
import com.example.musicspace.viewmodel.SignupViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        viewModel.signupState.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Signup successful.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showProgress(isLoading)
        }

        binding.btnCreateAccount.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val name = binding.editTextName.text.toString().trim()
            val phoneNumber = binding.editTextPhone.text.toString().trim()

            if (validateForm(email, password, confirmPassword, name, phoneNumber)) {
                viewModel.createAccount(email, password, name, phoneNumber)
            }
        }

        setupGoToLoginText()
    }

    private fun setupGoToLoginText() {
        val goToLoginText = getString(R.string.already_have_account_login)
        val spannableString = SpannableString(goToLoginText).apply {
            setSpan(UnderlineSpan(), 0, goToLoginText.length, 0)
        }
        binding.textGoToLogin.apply {
            text = spannableString
            setOnClickListener { navigateToLogin() }
        }
    }

    private fun validateForm(email: String, password: String, confirmPassword: String, name: String, phoneNumber: String): Boolean {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is required.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(phoneNumber) || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
