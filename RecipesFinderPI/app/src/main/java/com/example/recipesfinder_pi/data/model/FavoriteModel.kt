package com.example.recipesfinder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Represents a favorite relationship between a user and a recipe.
 *
 * @property idref The reference identifier of the recipe marked as favorite.
 * @property iduser The identifier of the user who marked the recipe as favorite.
 */
@Entity(tableName = "Favorite", primaryKeys = ["iduser", "idref"])
data class Favorite(
    @ColumnInfo(name = "idref")
    var idref: String = "",

    @ColumnInfo(name = "iduser")
    var iduser: String = ""
)