package com.example.recipesfinder.data.local.dao

import androidx.room.*
import com.example.recipesfinder.data.model.Comment

@Dao
interface CommentDao {

    @Insert
    fun insert(p: Comment): Long

    @Update
    fun update(p: Comment): Int

    @Delete
    fun delete(p: Comment)

    @Query("SELECT AVG(Rating) FROM Comment WHERE idref = :idref")
    fun getAverageRating(idref: String): Float

    @Query("SELECT * FROM Comment WHERE iduser = :iduser")
    fun getByIduser(iduser: String): List<Comment>

    @Query("SELECT * FROM Comment WHERE idref = :idref")
    fun getByIdref(idref: String): List<Comment>

    @Query("SELECT * FROM Comment")
    fun getAllComments(): List<Comment>
}



