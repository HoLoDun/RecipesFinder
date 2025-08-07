package com.example.recipesfinder.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Connection.RttTextStream
import android.widget.Toast
import com.example.recipesfinder.R
import com.example.recipesfinder.databinding.LoginActivityBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*

/**
 * Activity responsible for handling user login.
 *
 * This activity allows users to log in using their email and password via Firebase Authentication.
 * It also provides navigation to the registration screen and handles redirection if a user is already logged in.
 *
 * @constructor Creates a new instance of [LoginActivity].
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private val auth = FirebaseAuth.getInstance()

    /**
     * Called when the activity is starting.
     *
     * Initializes the view binding, sets up click listeners for UI elements such as the back button
     * and registration text, and invokes [handleLogin] to configure the login process.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState;
     *                           otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBack.setOnClickListener { finish() }

        binding.textRegister.setOnClickListener({
            startActivity(Intent(this, RegisterActivity::class.java))
        })

        handleLogin()
    }

    /**
     * Configures the login process.
     *
     * Sets a click listener on the login button that validates the email and password input.
     * If the inputs are valid, it attempts to sign in the user using Firebase Authentication.
     * On successful authentication, the user is navigated to [MainActivity] and the current activity is finished.
     * On failure, an appropriate error message is displayed based on the exception encountered.
     */
    private fun handleLogin() {
        binding.buttonLogin.setOnClickListener({
            val email = binding.editTextEmail.text.toString()
            val passwd = binding.editTextPasswd.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, R.string.email_empty , Toast.LENGTH_SHORT).show()
            } else if (passwd.isEmpty()){
                Toast.makeText(this, R.string.password_empty, Toast.LENGTH_SHORT).show()
            } else {

                auth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener({
                    if (it.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }).addOnFailureListener({
                    var msg = R.string.login_error
                    if (it is FirebaseAuthInvalidUserException) {
                        msg = R.string.user_not_exits
                    } else if (it is FirebaseAuthInvalidCredentialsException) {
                        msg = R.string.password_error
                    } else if (it is FirebaseNetworkException) {
                        msg = R.string.no_internet
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                })
            }
        })
    }

    /**
     * Called when the activity becomes visible to the user.
     *
     * Checks if a user is already authenticated. If a current user exists, navigates directly to [MainActivity]
     * and finishes this activity.
     */
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
