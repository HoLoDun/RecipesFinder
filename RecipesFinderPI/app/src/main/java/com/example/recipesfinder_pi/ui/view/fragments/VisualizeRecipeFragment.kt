package com.example.recipesfinder.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.recipesfinder.R
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesfinder.data.model.Favorite
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.databinding.FragmentVisualizeRecipeBinding
import com.example.recipesfinder.ui.view.LoginActivity
import com.example.recipesfinder.ui.view.adapter.ListIngredientAdapter
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel
import com.example.recipesfinder.ui.viewmodel.IngredientViewModel
import com.example.recipesfinder.ui.viewmodel.UsedIngredientViewModel
import com.example.recipesfinder.ui.viewmodel.FavoriteViewModel
import com.google.firebase.auth.*

/**
 * A Fragment that displays the details of a selected recipe.
 *
 * This fragment shows the recipe's name, description, preparation method, and associated ingredients.
 * It also allows the user to mark the recipe as a favorite, view the number of favorites, and navigate
 * to comments related to the recipe. The fragment interacts with several ViewModels:
 *
 * - [RecipeViewModel] to obtain recipe details.
 * - [FavoriteViewModel] to manage favorite recipes.
 * - [IngredientViewModel] and [UsedIngredientViewModel] to manage ingredients used in the recipe.
 *
 * Additionally, it handles favorite actions by checking if the recipe is already favorited and updates the UI accordingly.
 *
 * @constructor Creates an instance of [VisualizeRecipeFragment].
 */
class VisualizeRecipeFragment : Fragment() {

    // View binding for accessing layout elements.
    private var _binding: FragmentVisualizeRecipeBinding? = null
    private val binding get() = _binding!!

    // Firebase authentication instance.
    private val auth = FirebaseAuth.getInstance()

    // Recipe ID of the displayed recipe.
    private var recipeId: String = ""

    // Adapter to display the list of ingredients.
    private lateinit var adapter: ListIngredientAdapter

    // ViewModels for managing recipe, favorites, and ingredients.
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private lateinit var ingredientViewModel: IngredientViewModel
    private lateinit var usedingredientViewModel: UsedIngredientViewModel

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding and returns the root view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root View for the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisualizeRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after [onCreateView].
     *
     * Initializes adapters and ViewModels, configures the RecyclerView for ingredients,
     * and sets up various click listeners:
     * - Clicking the favorite icon toggles the recipe's favorite status.
     * - Clicking the comments button navigates to the CommentListFragment.
     * - Clicking the add comment button navigates to the AddCommentFragment if the user is logged in.
     *
     * Also sets up observers to update the UI when recipe data, favorites, or ingredients change.
     *
     * @param view The view returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListIngredientAdapter()

        ingredientViewModel = ViewModelProvider(requireActivity()).get(IngredientViewModel::class.java)
        usedingredientViewModel = ViewModelProvider(requireActivity()).get(UsedIngredientViewModel::class.java)
        favoriteViewModel = ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)

        adapter.ingvm = ingredientViewModel

        binding.rvIngredientes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredientes.adapter = adapter

        usedingredientViewModel.getUsedIngredientList().observe(viewLifecycleOwner, Observer { ingredientList ->
            adapter.updateRecipeList(ingredientList)
        })
        recipeViewModel.getRecipe().observe(viewLifecycleOwner, Observer {
            usedingredientViewModel.getByIdref(it.id.toString())
        })

        favoriteViewModel.numFavorites.observe(viewLifecycleOwner, Observer { count ->
            binding.tvNumFavorito.text = count.toString()
        })

        val currentUser = auth.currentUser
        binding.icFavorito.setOnClickListener {
            if (currentUser != null) {
                favoriteViewModel.getById(auth.currentUser?.uid.toString(), recipeId)
                favoriteViewModel.getIsSearched().observe(viewLifecycleOwner, Observer {
                    if (it == Constants.BD_MSGS.NOT_FOUND) {
                        try {
                            val f = Favorite().apply {
                                this.idref = recipeId
                                this.iduser = auth.currentUser?.uid.toString()
                            }
                            binding.icFavorito.setImageResource(R.drawable.ic_favorite)
                            favoriteViewModel.saveUser(f)
                            favoriteViewModel.updateNumFavorites(recipeId)
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), R.string.empty_number_msg, Toast.LENGTH_SHORT).show()
                        }
                    } else if (it == Constants.BD_MSGS.SUCCESS) {
                        favoriteViewModel.getFavorite().observe(viewLifecycleOwner, Observer { favorite ->
                            favoriteViewModel.deleteFavorite(favorite)
                            favoriteViewModel.updateNumFavorites(recipeId)
                            binding.icFavorito.setImageResource(R.drawable.ic_favorite_empty)
                        })
                    }
                })
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }

        binding.btnComentarios.setOnClickListener {
            recipeViewModel.getRecipe().observe(viewLifecycleOwner, Observer {
                val action = VisualizeRecipeFragmentDirections
                    .actionVisualizeRecipeFragmentToCommentListFragment(it.id.toString())
                findNavController().navigate(action)
            })
        }

        binding.btnAddComentario.setOnClickListener {
            recipeViewModel.getRecipe().observe(viewLifecycleOwner, Observer {
                if (currentUser != null) {
                    val action = VisualizeRecipeFragmentDirections
                        .actionVisualizeRecipeFragmentToAddCommentFragment(it.id.toString())
                    findNavController().navigate(action)
                } else {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
            })
        }

        setObserver()
    }

    /**
     * Clears all recipe detail texts and resets the dish image and favorite icon.
     */
    private fun clearTexts() {
        binding.tvNomePrato.text = ""
        binding.tvDescricao.text = ""
        binding.tvMetodoPreparo.text = ""
        binding.imgPrato.setImageResource(R.drawable.ic_image_placeholder)
        binding.icFavorito.setImageResource(R.drawable.ic_favorite)
    }

    /**
     * Sets up observers on LiveData objects from [RecipeViewModel], [FavoriteViewModel],
     * and [UsedIngredientViewModel] to update the UI with recipe details, favorite status, and ingredient list.
     *
     * - Observes the current recipe to update the recipe ID, name, description, method, and image.
     * - Observes the favorite search status to update the favorite icon.
     * - Observes the used ingredient list to update the ingredients adapter.
     */
    private fun setObserver() {
        recipeViewModel.getRecipe().observe(viewLifecycleOwner, Observer {
            recipeId = it.id.toString()
            binding.tvNomePrato.text = it.name
            binding.tvDescricao.text = it.description
            binding.tvMetodoPreparo.text = it.method
            favoriteViewModel.updateNumFavorites(recipeId)
            favoriteViewModel.getById(auth.currentUser?.uid.toString(), it.id.toString())
            val resId = Constants.USER_IMAGES.foodTypeMap[it.type]
            if (resId != null) {
                binding.imgPrato.setImageResource(resId)
            }
        })
        favoriteViewModel.getIsSearched().observe(viewLifecycleOwner, Observer {
            if (it == Constants.BD_MSGS.NOT_FOUND) {
                binding.icFavorito.setImageResource(R.drawable.ic_favorite_empty)
            } else if (it == Constants.BD_MSGS.SUCCESS) {
                binding.icFavorito.setImageResource(R.drawable.ic_favorite)
            }
        })

        usedingredientViewModel.getUsedIngredientList().observe(viewLifecycleOwner, Observer {
            adapter.updateRecipeList(it)
        })
    }

    /**
     * Called when the view previously created by [onCreateView] is being destroyed.
     *
     * Cleans up the view binding reference to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

