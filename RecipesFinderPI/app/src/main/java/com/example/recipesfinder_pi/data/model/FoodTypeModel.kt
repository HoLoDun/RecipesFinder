package com.example.recipesfinder.data.model

/**
 * Represents a food type with a name and an associated image.
 *
 * @property name The name of the food type.
 * @property image The resource identifier of the image representing the food type.
 */
data class FoodTypeModel(
    val name: String,
    val image: Int
)