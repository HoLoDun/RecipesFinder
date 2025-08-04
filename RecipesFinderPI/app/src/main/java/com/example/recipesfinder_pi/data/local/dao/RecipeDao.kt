package com.example.recipesfinder.data.local.dao

import androidx.room.*
import com.example.recipesfinder.data.model.Recipe

@Dao
interface RecipeDao {

    @Insert
    fun insert(p: Recipe): Long

    @Update
    fun update(p: Recipe): Int

    @Delete
    fun delete(p: Recipe)

    @Query("""
        SELECT R.*
        FROM Recipe R
        JOIN UsedIngredient IU ON R.id = IU.idref
        JOIN Ingredient I ON IU.IDing = I.id
        WHERE (:name IS NULL OR R.name LIKE '%' || :name || '%')
          AND ((:typeSize = 0) OR R.type IN (:type))
          AND (:calories IS 0 OR R.calories <= :calories)
          AND ((:ingredientsSize = 0) OR I.name IN (:ingredients))
        GROUP BY R.id
        ORDER BY R.name ASC

    """)
    fun getByQuery(
        name: String,
        type: List<String>,
        calories: Int,
        ingredients: List<String>,
        typeSize: Int,
        ingredientsSize: Int
    ): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE type = :type")
    fun getByType(type: String): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE (:name IS NULL OR name LIKE '%' || :name || '%')")
    fun getByQName(name: String): Recipe

    @Query("SELECT * FROM Recipe WHERE name = :name")
    fun getByName(name: String): Recipe

    @Query("SELECT R.* FROM Recipe R JOIN Favorite F ON R.id = F.idref WHERE F.iduser = :iduser")
    fun getByIduserFavorites(iduser: String): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE iduser = :iduser")
    fun getByIduser(iduser: String): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getById(id: Long): Recipe

    @Query("SELECT * FROM Recipe")
    fun getAllRecipes(): List<Recipe>
}


