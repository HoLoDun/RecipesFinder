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
import com.example.recipesfinder.databinding.FragmentSearchFilterBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListTypeRecipeFilterAdapter
import com.example.recipesfinder.ui.view.listener.ListTypeRecipeListener
import com.example.recipesfinder.ui.viewmodel.FoodTypeViewModel
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel

/**
 * Fragment that provides search filtering functionality.
 *
 * This fragment allows the user to filter recipes by entering a search query, selecting food types,
 * specifying a calorie range, and adding ingredients. The filtered results are retrieved via [RecipeViewModel.getByQuery]
 * and navigation is performed to display the results in a tertiary search fragment.
 *
 * The fragment uses a RecyclerView with a GridLayoutManager and a [ListTypeRecipeFilterAdapter] to display
 * available food types from the [FoodTypeViewModel]. It also provides UI controls for adding ingredients to the filter.
 */
class SearchFilterFragment : Fragment() {
    private var _binding: FragmentSearchFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodListVM: FoodTypeViewModel
    private lateinit var recipeVM: RecipeViewModel
    private val adapter = ListTypeRecipeFilterAdapter()
    private val ingredients = mutableListOf<String>()
    private val types = mutableListOf<String>()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding, sets up the [RecyclerView] with a grid layout of 3 columns,
     * initializes the [RecipeViewModel] and [FoodTypeViewModel], and sets up click listeners for the search,
     * filter, and ingredient addition buttons. It also observes changes in the food type list and displays them
     * using the adapter. When a food type is clicked, it is added to the filter criteria.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentSearchFilterBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter

        // Initialize RecipeViewModel for querying recipes.
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        // Set up click listener for the search button.
        binding.btnSearch.setOnClickListener {
            val ingList = if (ingredients.isNotEmpty()) ingredients else emptyList()
            val typeList = if (types.isNotEmpty()) types else emptyList()
            val query = binding.etSearch.text.toString()
            val caloriesText = binding.etCalories.text.toString()
            val calories = if (caloriesText.isNotEmpty()) caloriesText.toInt() else 0
            val ingredientSize = ingredients.size
            val typeSize = types.size
            recipeVM.getByQuery(query, typeList, calories, ingList, typeSize, ingredientSize)
            val action = PrimarySearchFragmentDirections
                .actionPrimarySearchFragmentToTerciarySearchFragment()
            findNavController().navigate(action)
        }

        // Set up click listener for the filter button.
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_primarySearchFragment_to_searchFilterFragment)
        }

        // Initialize FoodTypeViewModel and fetch all food types.
        foodListVM = ViewModelProvider(this).get(FoodTypeViewModel::class.java)
        foodListVM.getAllFoodTypes()

        // Set observers for food type list and status messages.
        setObserver()

        // Set listener for food type selection.
        val listener = object : ListTypeRecipeListener {
            override fun onClick(food: FoodTypeModel) {
                types.add(food.name)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.selected_type_filter_add, food.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        adapter.setListener(listener)

        // Set up click listener for adding an ingredient to the filter.
        binding.btnAddIngredient.setOnClickListener {
            val ingredient = binding.actvIngredients.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                ingredients.add(ingredient)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.selected_ingredient_add, ingredient),
                    Toast.LENGTH_SHORT
                ).show()
                binding.actvIngredients.setText("")
            } else {
                Toast.makeText(requireContext(), R.string.type_ingredient, Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch food types and re-set observers.
        foodListVM.getAllFoodTypes()
        setObserver()
        return binding.root
    }

    /**
     * Sets up observers for the FoodTypeViewModel's LiveData.
     *
     * Observes:
     * - The listing status via [FoodTypeViewModel.getIsListed] to display messages if no food types
     *   are found or if the search fails.
     * - The list of food types via [FoodTypeViewModel.getFoodTypeList] to update the adapter.
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
     * Called when the view created by [onCreateView] is about to be destroyed.
     *
     * This method cleans up the binding reference to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
