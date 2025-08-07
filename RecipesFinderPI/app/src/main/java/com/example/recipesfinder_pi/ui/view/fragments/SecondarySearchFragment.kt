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
import androidx.navigation.fragment.navArgs
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.databinding.FragmentSecondarySearchBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListRecipeAdapter
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel

/**
 * A fragment that performs a secondary search based on a selected food type.
 *
 * This fragment displays a list of recipes that belong to the food type provided as a navigation argument.
 * It allows the user to refine their search by entering a query or applying filters, and navigates to a tertiary
 * search fragment or a recipe visualization fragment based on user interactions.
 *
 * @property args Navigation arguments containing the selected food type name.
 * @constructor Creates an instance of [SecondarySearchFragment] using the layout resource [R.layout.fragment_secondary_search].
 */
class SecondarySearchFragment : Fragment(R.layout.fragment_secondary_search) {
    private var _binding: FragmentSecondarySearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeVM: RecipeViewModel
    private val adapter = ListRecipeAdapter()
    private val args: SecondarySearchFragmentArgs by navArgs()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * This method inflates the layout using view binding, sets up the RecyclerView with a [LinearLayoutManager]
     * and adapter, retrieves the food type name from the navigation arguments, and configures the [RecipeViewModel]
     * to fetch recipes for that food type. It also sets click listeners for search and filter buttons, and defines
     * an item click listener to navigate to the recipe visualization fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the inflated layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentSecondarySearchBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Retrieve the selected food type name from navigation arguments.
        val foodTypeName = args.foodTypeName

        // Configure the RecipeViewModel to fetch recipes for the selected food type.
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        recipeVM.getByType(foodTypeName)

        // Set up search button click listener.
        binding.btnSearch.setOnClickListener {
            val queryp = binding.etSearch.text.toString()
            recipeVM.getByQName(queryp)
            val action = SecondarySearchFragmentDirections
                .actionSecondarySearchFragmentToTerciarySearchFragment()
            findNavController().navigate(action)
        }

        // Set up filter button click listener.
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_primarySearchFragment_to_searchFilterFragment)
        }

        // Set listener for recipe item clicks to navigate to recipe visualization.
        val listener = object : OnRecipeListerner {
            override fun onClick(p: Recipe) {
                recipeVM.getById(p.id)
                findNavController().navigate(R.id.action_secondarySearchFragment_to_visualizeRecipeFragment)
            }
        }
        adapter.setListener(listener)
        setObserver()
        return binding.root
    }

    /**
     * Sets observers on LiveData from the RecipeViewModel.
     *
     * Observes:
     * - The listing status via [RecipeViewModel.getIsListed] to display a message if no recipes are found or if the search fails.
     * - The recipe list via [RecipeViewModel.getRecipeList] to update the adapter when new data is available.
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
     * Called when the view created by [onCreateView] is about to be destroyed.
     *
     * This method clears the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
