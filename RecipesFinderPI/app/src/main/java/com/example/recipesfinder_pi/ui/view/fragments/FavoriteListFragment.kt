package com.example.recipesfinder.ui.view.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.commit
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.recipesfinder.R
import androidx.navigation.fragment.navArgs
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.databinding.FragmentFavoriteListBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListRecipeAdapter
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel
import com.example.recipesfinder.ui.viewmodel.UserViewModel
import com.google.firebase.auth.*


/**
 * Fragment that displays the list of favorite recipes of the current user.
 *
 * This fragment retrieves and shows the list of recipes marked as favorites by the current user.
 * It uses a RecyclerView with a [ListRecipeAdapter] to display the recipes. When a recipe is clicked,
 * the corresponding recipe data is retrieved and the user is navigated to the recipe visualization screen.
 *
 * The fragment interacts with [RecipeViewModel] and [UserViewModel] to manage recipe and user data.
 */
class FavoriteListFragment : Fragment() {

    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeVM: RecipeViewModel
    private lateinit var userVM: UserViewModel
    private val adapter = ListRecipeAdapter()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding, sets up the RecyclerView with a [LinearLayoutManager]
     * and the adapter, initializes the [RecipeViewModel] to fetch the favorite recipes for the current user,
     * and sets up observers to update the UI. Also configures a click listener for recipe items which,
     * when clicked, fetches the recipe details and navigates to the recipe visualization fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Initialize RecipeViewModel and fetch favorite recipes for the current user.
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        recipeVM.getByIduserFavorites(auth.currentUser?.uid.toString())

        // Set observers to monitor recipe list and status messages.
        setObserver()

        // Set listener to handle recipe item clicks.
        val listener = object : OnRecipeListerner {
            override fun onClick(p: Recipe) {
                recipeVM.getById(p.id)
                findNavController().navigate(R.id.action_favoriteListFragment_to_visualizeRecipeFragment)
            }
        }
        adapter.setListener(listener)
        return binding.root
    }

    /**
     * Sets up observers for the RecipeViewModel's LiveData objects.
     *
     * Observes:
     * - The listing status [Constants.BD_MSGS] via [RecipeViewModel.getIsListed] to show appropriate messages.
     * - The list of recipes via [RecipeViewModel.getRecipeList] to update the adapter.
     */
    private fun setObserver() {
        recipeVM.getIsListed().observe(viewLifecycleOwner, Observer {
            if (it == Constants.BD_MSGS.NOT_FOUND) {
                Toast.makeText(activity, R.string.list_not_found, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(activity, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })

        recipeVM.getRecipeList().observe(viewLifecycleOwner, Observer {
            adapter.updateRecipeList(it)
        })
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     *
     * This method cleans up the view binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
