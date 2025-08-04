package com.example.recipesfinder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Represents a comment made on a recipe.
 *
 * @property id Unique identifier for the comment. This value is auto-generated.
 * @property Rating The rating of the recipe given as part of the comment.
 * @property Commentary The textual commentary associated with the comment.
 * @property iduser The identifier of the user who made the comment.
 * @property idref The reference identifier of the recipe to which the comment belongs.
 */
@Entity(tableName = "Comment", indices = [Index(value = ["id"], unique = true)])
data class Comment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "Rating")
    var Rating: Float = 0F,

    @ColumnInfo(name = "Commentary")
    var Commentary: String = "",

    @ColumnInfo(name = "iduser")
    var iduser: String = "",

    @ColumnInfo(name = "idref")
    var idref: String = ""
)