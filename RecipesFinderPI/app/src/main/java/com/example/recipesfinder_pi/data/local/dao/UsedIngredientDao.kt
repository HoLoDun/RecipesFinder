package com.example.recipesfinder.data.local.dao

import androidx.room.*
import com.example.recipesfinder.data.model.UsedIngredient

@Dao
interface UsedIngredientDao {

    @Insert
    fun insert(p: UsedIngredient): Long

    @Update
    fun update(p: UsedIngredient): Int

    @Delete
    fun delete(p: UsedIngredient)

    @Query("SELECT * FROM UsedIngredient WHERE idref = :idref")
    fun getByIdref(idref: String): List<UsedIngredient>

    @Query("SELECT * FROM UsedIngredient WHERE iding = :iding")
    fun getByIding(iding: String): List<UsedIngredient>

    @Query("SELECT * FROM UsedIngredient")
    fun getAllUsedIngredients(): List<UsedIngredient>
}
