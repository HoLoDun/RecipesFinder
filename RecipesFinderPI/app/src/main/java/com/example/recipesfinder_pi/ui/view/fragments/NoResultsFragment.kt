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
import com.example.recipesfinder.databinding.FragmentNoResultsBinding
import com.example.recipesfinder.ui.view.AddRecipeActivity
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListRecipeAdapter
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel

/**
 * A Fragment that is displayed when no search results are found.
 *
 * This fragment uses the layout defined in [R.layout.fragment_no_results] and allows the user
 * to either perform a new search or navigate to the search filter screen. It interacts with
 * [RecipeViewModel] to perform query operations and uses navigation components to navigate
 * between fragments.
 *
 *
 * @constructor Creates a new instance of [NoResultsFragment] with the specified layout.
 */
class NoResultsFragment : Fragment(R.layout.fragment_no_results) {
    private var _binding: FragmentNoResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeVM: RecipeViewModel

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the view using [FragmentNoResultsBinding], initializes [RecipeViewModel],
     * and sets click listeners for the search and filter buttons:
     * - The search button reads the query from an EditText, performs a query via [RecipeViewModel.getByQName],
     *   and navigates to [TerciarySearchFragment] using safe args.
     * - The filter button navigates to the [SearchFilterFragment].
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
        _binding = FragmentNoResultsBinding.inflate(inflater, container, false)
        recipeVM = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        binding.btnSearch.setOnClickListener {
            val queryp = binding.etSearch.text.toString()
            recipeVM.getByQName(queryp)
            val action = SecondarySearchFragmentDirections
                .actionSecondarySearchFragmentToTerciarySearchFragment()
            findNavController().navigate(action)
        }

        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_primarySearchFragment_to_searchFilterFragment)
        }

        return binding.root
    }

    /**
     * Called when the view previously created by onCreateView is being destroyed.
     *
     * This method cleans up the binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
