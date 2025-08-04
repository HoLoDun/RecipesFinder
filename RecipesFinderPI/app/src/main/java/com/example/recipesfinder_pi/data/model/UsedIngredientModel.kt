package com.example.recipesfinder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a used ingredient for a specific recipe.
 *
 * @property idref The reference identifier of the recipe where the ingredient is used.
 * @property iding The identifier of the ingredient used.
 * @property quantity The quantity of the ingredient used in the recipe.
 */
@Entity(tableName = "UsedIngredient", primaryKeys = ["idref", "iding"])
data class UsedIngredient(
    @ColumnInfo(name = "idref")
    var idref: String = "",

    @ColumnInfo(name = "iding")
    var iding: String = "",

    @ColumnInfo(name = "quantity")
    var quantity: String = ""

)