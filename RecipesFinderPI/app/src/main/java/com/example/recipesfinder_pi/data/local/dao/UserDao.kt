package com.example.recipesfinder.data.local.dao

import androidx.room.*
import com.example.recipesfinder.data.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(p: User): Long

    @Update
    fun update(p: User): Int

    @Delete
    fun delete(p: User)

    @Query("SELECT * FROM User WHERE iduser = :id")
    fun getById(id: String): User


    @Query("UPDATE user SET imageURL = :newImage WHERE iduser = :userId")
    fun updateUserImage(userId: String, newImage: String)


    @Query("SELECT * FROM User WHERE email = :email")
    fun getByEmail(email: String): User

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>
}