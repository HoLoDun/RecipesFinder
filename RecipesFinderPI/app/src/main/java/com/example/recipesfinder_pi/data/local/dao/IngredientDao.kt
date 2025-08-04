package com.example.recipesfinder.data.local.dao

import androidx.room.*
import com.example.recipesfinder.data.model.Ingredient

@Dao
interface IngredientDao {

    @Insert
    fun insert(p: Ingredient): Long

    @Update
    fun update(p: Ingredient): Int

    @Delete
    fun delete(p: Ingredient)

    @Query("SELECT * FROM Ingredient WHERE name = :query")
    fun getByQuery(query: String): Ingredient

    @Query("SELECT * FROM Ingredient WHERE id = :query")
    fun getByid(query: String): Ingredient

    @Query("SELECT * FROM Ingredient WHERE name LIKE '%' || :name || '%'")
    fun searchIngredients(name: String): List<Ingredient>

    @Query("SELECT * FROM Ingredient")
    fun getAllIngredients(): List<Ingredient>
}


