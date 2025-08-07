package com.example.recipesfinder.ui.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.ui.viewmodel.CommentViewModel
import com.example.recipesfinder.databinding.RecipeLineBinding
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.util.Constants

/**
 * ViewHolder responsible for displaying a recipe item in a RecyclerView.
 *
 * @property binding The view binding object for the recipe item layout.
 * @property listener A listener interface to handle recipe click events.
 * @property commentVM The ViewModel used to retrieve and calculate the average rating for a recipe.
 */
class ListRecipeViewHolder(
    private val binding: RecipeLineBinding,
    private val listener: OnRecipeListerner,
    private val commentVM: CommentViewModel
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the given recipe to the view holder.
     *
     * This method updates the UI components of the recipe item with the provided recipe's
     * name, description, and average rating, by getting the average of all the comments on the recipe.
     * It also sets an image resource based on the recipe type using the [Constants.USER_IMAGES.foodTypeMap].
     * When the recipe name is clicked, the corresponding click event is forwarded via the [listener].
     *
     * @param rec The [Recipe] object containing the details to be displayed.
     */
    fun bindVH(rec: Recipe) {
        binding.RecipeName.text = rec.name
        binding.RecipeRating.rating = commentVM.getAverageRating(rec.id.toString())
        binding.RecipeDescription.text = rec.description
        val resId = Constants.USER_IMAGES.foodTypeMap[rec.type]
        if (resId != null) {
            binding.RecipeImage.setImageResource(resId)
        }

        binding.RecipeName.setOnClickListener {
            listener.onClick(rec)
        }
    }
}

