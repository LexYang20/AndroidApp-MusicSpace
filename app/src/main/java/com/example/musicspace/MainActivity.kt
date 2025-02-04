package com.example.musicspace
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.appcompat.app.AppCompatActivity
import com.example.musicspace.databinding.ActivityMainBinding
import com.example.musicspace.view.LoginActivity
import com.example.musicspace.view.SignupActivity


//this is start-up page, please do not add your code/interface in this file
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this activity using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a SpannableString for the "Sign Up" button text and set an underline span on it
        val signUpText = SpannableString(getString(R.string.sign_up))
        signUpText.setSpan(UnderlineSpan(), 0, signUpText.length, 0)
        binding.btnGoToSignup.text = signUpText

        // Create a SpannableString for the "Login" button text and set an underline span on it
        val loginText = SpannableString(getString(R.string.login))
        loginText.setSpan(UnderlineSpan(), 0, loginText.length, 0)
        binding.btnGoToLogin.text = loginText

        // Set onClickListeners for the buttons to navigate to the respective activities
        binding.btnGoToSignup.setOnClickListener {
            // Start the SignupActivity when the "Sign Up" button is clicked
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnGoToLogin.setOnClickListener {
            // Start the LoginActivity when the "Login" button is clicked
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
