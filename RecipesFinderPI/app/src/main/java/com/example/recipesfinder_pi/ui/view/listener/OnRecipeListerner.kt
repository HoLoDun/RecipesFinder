package com.example.recipesfinder.ui.view.listener

import com.example.recipesfinder.data.model.Recipe

/**
 * Interface definition for a callback to be invoked when a recipe item is clicked.
 */
interface OnRecipeListerner {
    /**
     * Called when a recipe is clicked.
     *
     * @param food The [Recipe] object that was clicked.
     */
    fun onClick(food: Recipe)
}
