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
import com.example.musicspace.databinding.ActivityLoginBinding
import com.example.musicspace.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.loginSuccess.observe(this) { success ->
            if (success) navigateToBrowse()
            else showProgress(false)
        }

        viewModel.loginError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showProgress(isLoading)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            if (validateForm(email, password)) {
                viewModel.signIn(email, password)
            }
        }

        binding.textForgotPassword.setOnClickListener {
            navigateToPasswordReset()
        }

        setUnderlineText()
    }

    private fun setUnderlineText() {
        val forgotPasswordText = getString(R.string.forgot_password)
        val spannableString = SpannableString(forgotPasswordText).apply {
            setSpan(UnderlineSpan(), 0, forgotPasswordText.length, 0)
        }
        binding.textForgotPassword.text = spannableString
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToBrowse() {
        val intent = Intent(this, BrowseActivity::class.java) // Assume BrowseActivity is the main activity
        startActivity(intent)
        finish()
    }

    private fun navigateToPasswordReset() {
        val intent = Intent(this, PasswordResetActivity::class.java)
        startActivity(intent)
    }
}
