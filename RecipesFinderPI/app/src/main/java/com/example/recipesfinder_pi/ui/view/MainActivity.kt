package com.example.recipesfinder.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.recipesfinder.databinding.ActivityMainBinding
import com.google.firebase.auth.*

/**
 * Main activity of the RecipesFinder application.
 *
 * This activity serves as the entry point of the app, providing navigation to the core features
 * such as adding a recipe, searching recipes, accessing settings, and logging in. Depending on the
 * user's authentication state, clicking on the "add" icon navigates to either the recipe addition
 * screen or the login screen.
 *
 * The activity uses ViewBinding ([ActivityMainBinding]) for accessing and managing its UI components.
 *
 * @constructor Creates an instance of [MainActivity].
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    /**
     * Called when the activity is starting.
     *
     * Inflates the layout using view binding, sets click listeners for the UI elements, and defines
     * the navigation flow based on the current user's authentication state.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState;
     *                           otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageAdd.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                startActivity(Intent(this, AddRecipeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        binding.imageSearch.setOnClickListener {
            startActivity(Intent(this, MainSearchActivity::class.java))
        }
        binding.imageList.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    /**
     * Handles click events for views registered with this [View.OnClickListener].
     *
     * In this activity, click events are handled by setting individual listeners directly on the views.
     * This method is provided to satisfy the interface and may be extended in the future if needed.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        // No implementation required as individual click listeners are set in onCreate.
    }
}
