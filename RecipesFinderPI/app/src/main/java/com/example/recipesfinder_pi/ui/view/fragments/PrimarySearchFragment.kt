package com.example.recipesfinder.ui.view.fragments


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.recipesfinder.R
import com.example.recipesfinder.data.model.FoodTypeModel
import com.example.recipesfinder.databinding.FragmentPrimarySearchBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListTypeRecipeAdapter
import com.example.recipesfinder.ui.view.listener.ListTypeRecipeListener
import com.example.recipesfinder.ui.viewmodel.FoodTypeViewModel
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel

/**
 * Fragment responsible for the primary search functionality.
 *
 * This fragment displays a grid of food types using a RecyclerView with a GridLayoutManager.
 * It allows the user to perform a search by entering a query and navigating to the tertiary search screen,
 * or to filter the search by navigating to the search filter screen.
 * Additionally, it sets up a click listener on each food type item to navigate to the secondary search screen
 * with the selected food type name.
 *
 * The fragment interacts with [FoodTypeViewModel] to retrieve the list of food types and with
 * [RecipeViewModel] to perform recipe queries based on the user's search input.
 */
class PrimarySearchFragment : Fragment() {

    private var _binding: FragmentPrimarySearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodListVM: FoodTypeViewModel
    private lateinit var recipeVM: RecipeViewModel
    private val adapter = ListTypeRecipeAdapter()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding, configures the RecyclerView with a grid layout,
     * initializes the view models, and sets click listeners for the search and filter buttons.
     * It also sets up observers to update the UI with food type data and configures item click listeners
     * to navigate to the secondary search screen based on the selected food type.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root View of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPrimarySearchBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        // Initialize RecipeViewModel for querying recipes.
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        binding.btnSearch.setOnClickListener {
            val queryp = binding.etSearch.text.toString()
            recipeVM.getByQName(queryp)
            val action = PrimarySearchFragmentDirections
                .actionPrimarySearchFragmentToTerciarySearchFragment()
            findNavController().navigate(action)
        }
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_primarySearchFragment_to_searchFilterFragment)
        }

        // Initialize FoodTypeViewModel for managing food types.
        foodListVM = ViewModelProvider(this).get(FoodTypeViewModel::class.java)
        foodListVM.getAllFoodTypes()

        // Set observers for food types and list status messages.
        setObserver()

        // Set item click listener for food type items.
        val listener = object : ListTypeRecipeListener {
            override fun onClick(food: FoodTypeModel) {
                val action = PrimarySearchFragmentDirections
                    .actionPrimarySearchFragmentToSecondarySearchFragment(food.name)
                findNavController().navigate(action)
            }
        }
        adapter.setListener(listener)
        return binding.root
    }

    /**
     * Sets up observers for the FoodTypeViewModel's LiveData objects.
     *
     * Observes the list status via [FoodTypeViewModel.getIsListed] to display messages if the food type list
     * is not found or if there is a search failure. Also, observes [FoodTypeViewModel.getFoodTypeList] to update
     * the adapter with the latest list of food types.
     */
    private fun setObserver() {
        foodListVM.getIsListed().observe(viewLifecycleOwner, Observer {
            if (it == Constants.BD_MSGS.NOT_FOUND) {
                Toast.makeText(activity, R.string.list_not_found, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(activity, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })

        foodListVM.getFoodTypeList().observe(viewLifecycleOwner, Observer {
            adapter.updateFoodTypeList(it)
        })
    }

    /**
     * Called when the view previously created by [onCreateView] is being destroyed.
     *
     * Cleans up the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
