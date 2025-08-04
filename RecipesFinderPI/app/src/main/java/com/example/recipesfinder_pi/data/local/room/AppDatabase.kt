package com.example.recipesfinder.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipesfinder.data.local.dao.RecipeDao
import com.example.recipesfinder.data.local.dao.UsedIngredientDao
import com.example.recipesfinder.data.local.dao.UserDao
import com.example.recipesfinder.data.local.dao.FavoriteDao
import com.example.recipesfinder.data.local.dao.IngredientDao
import com.example.recipesfinder.data.local.dao.CommentDao
import com.example.recipesfinder.data.model.*

@Database(
    entities = [User::class, Recipe::class, Ingredient::class, Favorite::class, Comment::class, UsedIngredient::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun RecipeDao(): RecipeDao
    abstract fun FavoriteDao(): FavoriteDao
    abstract fun UsedIngredientDao(): UsedIngredientDao
    abstract fun IngredientDao(): IngredientDao
    abstract fun CommentDao(): CommentDao


    companion object {
        private lateinit var INSTANCE: AppDatabase
        fun getDatabase(context: Context): AppDatabase {

            if(!::INSTANCE.isInitialized) {

                synchronized(AppDatabase::class) {

                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "recipesfinder.db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
