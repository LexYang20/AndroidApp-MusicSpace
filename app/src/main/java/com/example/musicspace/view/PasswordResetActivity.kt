package com.example.musicspace.view
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.musicspace.R
import com.example.musicspace.databinding.ActivityPasswordResetBinding
import com.example.musicspace.viewmodel.PasswordResetViewModel

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordResetBinding
    private lateinit var viewModel: PasswordResetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PasswordResetViewModel::class.java]

        viewModel.resetEmailStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Reset email sent!", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.SendButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            if (email.isNotEmpty() && isEmailValid(email)) {
                viewModel.sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        if (email.length < 12) {
            return false
        }
        val emailPattern = getString(R.string.a_za_z0_9_a_za_z0_9_a_za_z_2_6)
        return email.matches(emailPattern.toRegex())
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
