package com.example.recipesfinder.ui.view.listener

import com.example.recipesfinder.data.model.FoodTypeModel

/**
 * Listener interface for handling click events on recipe type items.
 *
 * Implement this interface to be notified when a recipe type is clicked.
 */
interface ListTypeRecipeListener {

    /**
     * Called when a recipe type item is clicked.
     *
     * @param food The [FoodTypeModel] representing the clicked recipe type.
     */
    fun onClick(food: FoodTypeModel)
}
