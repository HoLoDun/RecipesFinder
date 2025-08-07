package com.example.recipesfinder.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.recipesfinder.R
import com.example.recipesfinder.databinding.MainUserActivityBinding
import com.google.firebase.auth.*
/**
 * Activity for managing the main user interface of the application.
 *
 * This activity provides navigation between different user-related screens such as the user profile,
 * user's recipes, and favorite recipes. It also offers navigation controls via icons (home, search,
 * profile, and back) and updates the title of the screen based on the current destination in the navigation graph.
 *
 * The activity uses ViewBinding ([MainUserActivityBinding]) for accessing UI elements and hosts a [NavHostFragment]
 * to manage fragment navigation.
 *
 * @constructor Creates an instance of [MainUserActivity].
 */
class MainUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: MainUserActivityBinding
    private val auth = FirebaseAuth.getInstance()

    /**
     * Called when the activity is starting.
     *
     * Inflates the layout using view binding, hides the action bar, sets up click listeners for
     * navigation icons, and configures the navigation controller to update the title based on the current fragment destination.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied; otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainUserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.imageBack.setOnClickListener(this)
        binding.ivHome.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        binding.ivProfile.setOnClickListener(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
        val navController = navHostFragment?.navController
        if (navController == null) {
            // Handles the case where the NavController is not found by logging an error.
            Log.e("MainUserActivity", getString(R.string.navcontroller_notfound))
            return
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.userProfileFragment -> binding.tvTitulo.text = getString(R.string.user_profile)
                R.id.userRecipeListFragment -> binding.tvTitulo.text = getString(R.string.user_recipes)
                R.id.favoriteListFragment -> binding.tvTitulo.text = getString(R.string.favorite_list)
                R.id.visualizeRecipeFragment -> binding.tvTitulo.text = getString(R.string.visualize_recipe)
                R.id.addCommentFragment -> binding.tvTitulo.text = getString(R.string.add_comment)
                R.id.commentListFragment -> binding.tvTitulo.text = getString(R.string.comments)
                // Add other cases as needed.
                else -> binding.tvTitulo.text = getString(R.string.user_profile)
            }
        }
    }

    /**
     * Handles click events for navigation.
     *
     * - If the back image is clicked, attempts to pop the back stack; if unable to do so, finishes the activity.
     * - If the home icon is clicked, navigates to [MainActivity].
     * - If the search icon is clicked, navigates to [MainSearchActivity].
     * - If the profile icon is clicked, re-launches [MainUserActivity].
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View) {
        if (view.id == R.id.image_back) {
            val navController = findNavController(R.id.fragmentContainerView)
            if (!navController.popBackStack()) {
                finish()
            }
        } else if (view.id == R.id.iv_home) {
            startActivity(Intent(this, MainActivity::class.java))
        } else if (view.id == R.id.iv_search) {
            startActivity(Intent(this, MainSearchActivity::class.java))
        } else if (view.id == R.id.iv_profile) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                startActivity(Intent(this, MainUserActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}
