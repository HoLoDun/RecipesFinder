package com.example.recipesfinder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a recipe in the application.
 *
 * @property id Unique identifier for the recipe. This value is auto-generated.
 * @property name The name of the recipe. Must be unique.
 * @property description A short description of the recipe.
 * @property method The preparation method for the recipe.
 * @property iduser The identifier of the user who created the recipe.
 * @property type The category or type of the recipe.
 * @property calories The number of calories contained in the recipe.
 * @property imageURL A URL pointing to an image representing the recipe.
 */
@Entity(tableName = "Recipe", indices = [Index(value = ["name"], unique = true)])
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "method")
    var method: String = "",

    @ColumnInfo(name = "iduser")
    var iduser: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "calories")
    var calories: Int = 0,

    @ColumnInfo(name = "imageURL")
    var imageURL: String = ""
)