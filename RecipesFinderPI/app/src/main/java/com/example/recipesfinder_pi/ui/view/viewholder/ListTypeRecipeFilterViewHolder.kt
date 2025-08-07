package com.example.recipesfinder.ui.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.TypeRecipeFilterLineBinding
import com.example.recipesfinder.ui.view.listener.ListTypeRecipeListener
import com.example.recipesfinder.data.model.FoodTypeModel

/**
 * A ViewHolder for displaying a food type item in a RecyclerView for the search filter fragment.
 *
 * @property binding The binding instance for the food type filter item layout.
 * @property listener The listener that handles click events on the food type filter item.
 */
class FoodTypeFilterViewHolder(
    private val binding: TypeRecipeFilterLineBinding,
    private val listener: ListTypeRecipeListener
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the provided [FoodTypeModel] to the view, setting the food type name and handling click events.
     * It differs for the normal food type ViewHolder as it had the image removed for batter display
     * in a small space.
     *
     * @param food The [FoodTypeModel] containing the food type data to display.
     */
    fun bindVH(food: FoodTypeModel) {
        binding.textFoodType.text = food.name

        binding.root.setOnClickListener {
            listener.onClick(food)
        }
    }
}
