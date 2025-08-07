package com.example.recipesfinder.ui.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.recipesfinder.R
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.databinding.FragmentUserRecipeListBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListRecipeAdapter
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel
import com.example.recipesfinder.ui.viewmodel.UserViewModel
import com.google.firebase.auth.*


/**
 * Fragment that displays a list of recipes created by the current user.
 *
 * This fragment retrieves and shows the list of recipes associated with the authenticated user
 * using [RecipeViewModel]. The recipes are displayed in a [RecyclerView] using a [ListRecipeAdapter].
 * When a recipe item is clicked, the fragment retrieves the recipe details and navigates to the recipe
 * visualization screen.
 *
 * @constructor Creates an instance of [UserRecipeListFragment].
 */
class UserRecipeListFragment : Fragment() {

    private var _binding: FragmentUserRecipeListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeVM: RecipeViewModel
    private lateinit var userVM: UserViewModel
    private val adapter = ListRecipeAdapter()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding, sets up the [RecyclerView] with a [LinearLayoutManager]
     * and adapter, and initializes [RecipeViewModel] to fetch recipes created by the current user.
     * It also sets up observers to update the UI with the list of recipes and a click listener on
     * recipe items to navigate to the recipe visualization screen.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentUserRecipeListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Initialize RecipeViewModel and retrieve recipes for the current user.
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        recipeVM.getByIduser(auth.currentUser?.uid.toString())

        // Set observers to update UI based on recipe list and status messages.
        setObserver()

        // Set listener for recipe item clicks to navigate to the recipe visualization fragment.
        val listener = object : OnRecipeListerner {
            override fun onClick(p: Recipe) {
                recipeVM.getById(p.id)
                findNavController().navigate(R.id.action_userRecipeListFragment_to_visualizeRecipeFragment)
            }
        }
        adapter.setListener(listener)
        setObserver()
        return binding.root
    }

    /**
     * Sets up observers on the RecipeViewModel's LiveData objects.
     *
     * Observes:
     * - The listing status via [RecipeViewModel.getIsListed] to display a message if no recipes are found or
     *   if the search fails.
     * - The list of recipes via [RecipeViewModel.getRecipeList] to update the adapter when new data is available.
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
     * Called when the view previously created by [onCreateView] is being destroyed.
     *
     * Cleans up the binding reference to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
