package com.example.recipesfinder.data.local.dao

import androidx.room.*
import com.example.recipesfinder.data.model.Favorite

@Dao
interface FavoriteDao {

    @Insert
    fun insert(p: Favorite): Long

    @Update
    fun update(p: Favorite): Int

    @Delete
    fun delete(p: Favorite)

    @Query("SELECT * FROM Favorite WHERE iduser = :iduser")
    fun getByIduser(iduser: String): List<Favorite>

    @Query("SELECT * FROM Favorite WHERE iduser = :iduser AND idref = :idref")
    fun getById(iduser: String, idref: String): Favorite

    @Query("""SELECT COUNT(*) AS total_favoritos
              FROM Favorite
              WHERE idref = :idref
              GROUP BY idref
              ORDER BY total_favoritos DESC;""")
    fun getNumFavorite(idref: String): Int

    @Query("SELECT * FROM Favorite")
    fun getAllFavorites(): List<Favorite>
}



