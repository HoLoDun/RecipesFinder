package com.example.recipesfinder.ui.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.IngredientLineBinding
import com.example.recipesfinder.data.model.UsedIngredient
import com.example.recipesfinder.ui.viewmodel.IngredientViewModel

/**
 * ViewHolder for displaying an ingredient item within a list.
 *
 * @property binding The binding object for the ingredient line layout.
 * @property ingredientVM The ViewModel used to retrieve ingredient details.
 */
class ListIngredientViewHolder(
    private val binding: IngredientLineBinding,
    private val ingredientVM: IngredientViewModel
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the data of a used ingredient to the UI components.
     *
     * This function populates the UI elements with the ingredient used in a recipe data,
     *
     * @param food The [UsedIngredient] object containing the ingredient details.
     */
    fun bindVH(food: UsedIngredient) {
        binding.TextIngrediente.text = ingredientVM.getByid(food.iding)
        binding.TextQuantidade.text = food.quantity
    }
}
