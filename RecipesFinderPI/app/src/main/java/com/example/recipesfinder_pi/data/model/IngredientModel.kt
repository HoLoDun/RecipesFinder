package com.example.recipesfinder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an ingredient used in recipes.
 *
 * @property id Unique identifier for the ingredient, auto-generated.
 * @property name The name of the ingredient.
 */
@Entity(tableName = "Ingredient", indices = [Index(value = ["name"], unique = true)])
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = ""
)