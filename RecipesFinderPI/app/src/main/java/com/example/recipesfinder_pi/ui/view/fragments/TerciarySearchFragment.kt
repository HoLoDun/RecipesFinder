package com.example.recipesfinder.ui.view.fragments

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
import com.example.recipesfinder.databinding.FragmentTerciarySearchBinding
import com.example.recipesfinder.ui.view.AddRecipeActivity
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListRecipeAdapter
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel

/**
 * Fragment that displays a tertiary search interface for recipes, said interface displays the result of the searchbar and filter.
 *
 * This fragment uses the layout defined in [R.layout.fragment_secondary_search] (note: the layout resource name may be misnamed)
 * and provides functionality to search for recipes by query or filter and to navigate to the recipe visualization screen.
 *
 * The fragment uses a RecyclerView with a [ListRecipeAdapter] to display the list of recipes.
 * It interacts with [RecipeViewModel] to perform search queries and retrieve recipe lists.
 *
 * When a recipe is clicked, the fragment retrieves detailed recipe information and navigates to the recipe visualization screen.
 *
 * @constructor Creates an instance of [TerciarySearchFragment] with the specified layout.
 */
class TerciarySearchFragment : Fragment(R.layout.fragment_terciary_search) {

    private var _binding: FragmentTerciarySearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeVM: RecipeViewModel
    private val adapter = ListRecipeAdapter()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding, configures the [RecyclerView] with a [LinearLayoutManager] and adapter,
     * initializes the [RecipeViewModel], and sets click listeners for search and filter buttons.
     * It also sets up observers to update the UI based on search results.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root View of the inflated layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentTerciarySearchBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Initialize RecipeViewModel for querying recipes.
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        // Set up search button to perform a query and navigate.
        binding.btnSearch.setOnClickListener {
            val queryp = binding.etSearch.text.toString()
            recipeVM.getByQName(queryp)
            val action = PrimarySearchFragmentDirections
                .actionPrimarySearchFragmentToTerciarySearchFragment()
            findNavController().navigate(action)
        }

        // Set up filter button to navigate to the search filter screen.
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_primarySearchFragment_to_searchFilterFragment)
        }

        // Set up observers to update the UI based on recipe list and search status.
        setObserver()

        // Configure item click listener for recipe items.
        val listener = object : OnRecipeListerner {
            override fun onClick(p: Recipe) {
                recipeVM.getById(p.id)
                findNavController().navigate(R.id.action_terciarySearchFragment_to_visualizeRecipeFragment)
            }
        }
        adapter.setListener(listener)
        return binding.root
    }

    /**
     * Sets up observers for the RecipeViewModel's LiveData objects.
     *
     * Observes the listing status via [RecipeViewModel.getIsListed]:
     * - If no recipes are found, navigates to the no-results fragment.
     * - If the search fails, displays an error toast.
     * - If the search is successful, displays a success toast.
     *
     * Also observes the recipe list via [RecipeViewModel.getRecipeList] to update the adapter.
     */
    private fun setObserver() {
        recipeVM.getIsListed().observe(viewLifecycleOwner, Observer {
            if (it == Constants.BD_MSGS.NOT_FOUND) {
                findNavController().navigate(R.id.action_terciaryRecipeFragment_to_noResultsFragment)
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(activity, R.string.fail_search, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.SUCCESS) {
                Toast.makeText(activity, R.string.success_search, Toast.LENGTH_SHORT).show()
            }
        })

        recipeVM.getRecipeList().observe(viewLifecycleOwner, Observer {
            adapter.updateRecipeList(it)
        })
    }

    /**
     * Called when the view previously created by [onCreateView] is being destroyed.
     *
     * This method cleans up the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
