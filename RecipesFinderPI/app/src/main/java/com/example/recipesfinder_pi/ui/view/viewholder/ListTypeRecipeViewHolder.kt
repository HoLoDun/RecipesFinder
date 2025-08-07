package com.example.recipesfinder.ui.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.TypeRecipeLineBinding
import com.example.recipesfinder.ui.view.listener.ListTypeRecipeListener
import com.example.recipesfinder.data.model.FoodTypeModel

/**
 * A ViewHolder for displaying a food type item in a RecyclerView.
 *
 * @property binding The binding instance for the food type item layout.
 * @property listener The listener used to handle click events on the food type item.
 */
class FoodTypeViewHolder(
    private val binding: TypeRecipeLineBinding,
    private val listener: ListTypeRecipeListener
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the provided [FoodTypeModel] to the corresponding views.
     *
     * @param food The [FoodTypeModel] containing the food type data.
     */
    fun bindVH(food: FoodTypeModel) {
        binding.textFoodType.text = food.name
        binding.imageFoodType.setImageResource(food.image)

        binding.root.setOnClickListener {
            listener.onClick(food)
        }
    }
}
