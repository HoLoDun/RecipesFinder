package com.example.recipesfinder.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.recipesfinder.R
import com.example.recipesfinder.util.Constants
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.data.model.User
import com.example.recipesfinder.databinding.RegisterActivityBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.example.recipesfinder.ui.viewmodel.UserViewModel

/**
 * Activity responsible for user registration.
 *
 * This activity allows a new user to register by providing their first name, last name, nickname,
 * email, and password. It validates the input fields and uses Firebase Authentication to create the user.
 * On successful registration, it saves the user data into the local database via [UserViewModel] and
 * navigates to the [LoginActivity].
 *
 * @constructor Creates an instance of [RegisterActivity].
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding
    private val auth = FirebaseAuth.getInstance()
    private lateinit var userVM: UserViewModel

    /**
     * Called when the activity is starting.
     *
     * Inflates the layout using view binding, sets click listeners for navigation elements,
     * initializes the [UserViewModel], and calls [handleRegister] to configure registration logic.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied; otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userVM = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.textRegister.setOnClickListener({
            startActivity(Intent(this, LoginActivity::class.java))
        })
        binding.imageBack.setOnClickListener { finish() }
        handleRegister()
    }

    /**
     * Configures the registration process.
     *
     * Sets a click listener on the register button that validates the user inputs (first name,
     * last name, nickname, email, and password). If the inputs are valid and the two passwords match,
     * it creates a new user using Firebase Authentication. On successful registration, it saves the
     * user data via [saveUserData], shows a success message, navigates to [LoginActivity] and finishes
     * the current activity. In case of failure, an appropriate error message is shown.
     */
    private fun handleRegister() {
        binding.buttonRegister.setOnClickListener({
            val name = binding.editTextName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val nickName = binding.editTextNickName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val passwd1 = binding.editTextPasswd1.text.toString()
            val passwd2 = binding.editTextPasswd2.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, R.string.type_firstname, Toast.LENGTH_SHORT).show()
            } else if (lastName.isEmpty()) {
                Toast.makeText(this, R.string.type_lastname, Toast.LENGTH_SHORT).show()
            } else if (nickName.isEmpty()) {
                Toast.makeText(this, R.string.type_nickname, Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, R.string.type_email, Toast.LENGTH_SHORT).show()
            } else if (passwd1.isEmpty()) {
                Toast.makeText(this, R.string.type_password, Toast.LENGTH_SHORT).show()
            } else if (passwd2.isEmpty()) {
                Toast.makeText(this, R.string.type_password2, Toast.LENGTH_SHORT).show()
            } else if (passwd1 != passwd2) {
                Toast.makeText(this, R.string.mismatch_password, Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, passwd1).addOnCompleteListener({
                    if (it.isSuccessful) {
                        saveUserData(name, lastName, nickName, email)
                        Toast.makeText(this, R.string.sucess_register, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }).addOnFailureListener({
                    var msg = R.string.fail_register
                    if (it is FirebaseAuthInvalidCredentialsException) {
                        msg = R.string.valid_email
                    } else if (it is FirebaseAuthWeakPasswordException) {
                        msg = R.string.short_password
                    } else if (it is FirebaseAuthUserCollisionException) {
                        msg = R.string.duplicate_email
                    } else if (it is FirebaseNetworkException) {
                        msg = R.string.no_internet
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                })
            }
        })
    }

    /**
     * Saves the user's data in the local database.
     *
     * Retrieves the current user's UID from Firebase Authentication, randomly selects a profile image,
     * creates a [User] object with the provided information, and saves it using [UserViewModel].
     *
     * @param name The first name of the user.
     * @param lastName The last name of the user.
     * @param nick The nickname of the user.
     * @param email The email address of the user.
     */
    private fun saveUserData(name: String, lastName: String, nick: String, email: String) {
        val currUserId = auth.currentUser?.uid
        if (currUserId != null) {
            try {
                val randomImage = Constants.USER_IMAGES.profileImages.random()
                val u = User().apply {
                    this.firstname = name
                    this.lastname = lastName
                    this.nickname = nick
                    this.email = email
                    this.imageURL = randomImage
                    this.iduser = auth.currentUser?.uid.toString()
                }
                userVM.saveUser(u)
            } catch (e: Exception) {
                Log.e("Exception", getString(R.string.register_error), e)
                Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sets observers for LiveData objects from [UserViewModel].
     *
     * Observes the save status of user data and displays appropriate toast messages based on the outcome.
     * - [Constants.BD_MSGS.SUCCESS]: Registration successful.
     * - [Constants.BD_MSGS.FAIL]: Registration failed.
     * - [Constants.BD_MSGS.CONSTRAINT]: A constraint error occurred (e.g., duplicate email).
     */
    private fun setObserver() {
        userVM.getIsSaved().observe(this, Observer {
            if (it == Constants.BD_MSGS.SUCCESS) {
                Toast.makeText(this, R.string.sucess_create, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(this, R.string.fail_create, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.CONSTRAINT) {
                Toast.makeText(this, R.string.other, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
