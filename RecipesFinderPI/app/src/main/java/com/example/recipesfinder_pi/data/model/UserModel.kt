package com.example.recipesfinder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a user in the application.
 *
 * @property id Auto-generated unique identifier for the User entity.
 * @property iduser Unique identifier provided by the authentication system.
 * @property firstname The user's first name.
 * @property lastname The user's last name.
 * @property email The user's email address.
 * @property imageURL The URL of the user's profile image.
 * @property nickname The user's nickname.
 */
@Entity(tableName = "User", indices = [Index(value = ["id"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "iduser")
    var iduser: String = "",

    @ColumnInfo(name = "firstname")
    var firstname: String = "",

    @ColumnInfo(name = "lastname")
    var lastname: String = "",

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "imageURL")
    var imageURL: String = "",

    @ColumnInfo(name = "nickname")
    var nickname: String = ""
)