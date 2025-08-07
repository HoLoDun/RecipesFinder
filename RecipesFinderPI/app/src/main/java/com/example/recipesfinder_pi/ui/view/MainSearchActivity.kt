package com.example.recipesfinder.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.recipesfinder.R
import com.example.recipesfinder.databinding.MainSearchActivityBinding
import com.google.firebase.auth.*


/**
 * Activity that provides the main search interface for the application.
 *
 * This activity hosts a [NavHostFragment] for various search-related fragments and provides
 * navigation through click listeners on UI elements such as the back button, home, search, and profile icons.
 * It also updates the title based on the current destination in the navigation graph.
 *
 * @constructor Creates an instance of [MainSearchActivity].
 */
class MainSearchActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: MainSearchActivityBinding
    private val auth = FirebaseAuth.getInstance()

    /**
     * Called when the activity is starting.
     *
     * Inflates the layout using view binding, hides the action bar, sets up click listeners for
     * various navigation icons, and configures the navigation controller to update the title based
     * on the current destination.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the most recent data supplied in onSaveInstanceState;
     *                           otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainSearchActivityBinding.inflate(layoutInflater)
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
            // Handle the case where the NavController is not found, logging the error.
            Log.e("MainSearchActivity", getString(R.string.navcontroller_notfound))
            return
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.primarySearchFragment -> binding.tvTitulo.text = getString(R.string.search)
                R.id.secondarySearchFragment -> binding.tvTitulo.text = getString(R.string.search)
                R.id.terciarySearchFragment -> binding.tvTitulo.text = getString(R.string.search)
                R.id.searchFilterFragment -> binding.tvTitulo.text = getString(R.string.search)
                R.id.visualizeRecipeFragment -> binding.tvTitulo.text = getString(R.string.visualize_recipe)
                R.id.addCommentFragment -> binding.tvTitulo.text = getString(R.string.add_comment)
                R.id.commentListFragment -> binding.tvTitulo.text = getString(R.string.comments)
                R.id.noResultsFragment -> binding.tvTitulo.text = getString(R.string.search)
                // Add other cases as needed.
                else -> binding.tvTitulo.text = getString(R.string.search)
            }
        }
    }

    /**
     * Handles click events for the registered views.
     *
     * - If the back image is clicked, attempts to pop the back stack of the NavController; if not possible,
     *   finishes the activity.
     * - If the home icon is clicked, navigates to [MainActivity].
     * - If the search icon is clicked, re-launches [MainSearchActivity].
     * - If the profile icon is clicked, navigates to [MainUserActivity].
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
