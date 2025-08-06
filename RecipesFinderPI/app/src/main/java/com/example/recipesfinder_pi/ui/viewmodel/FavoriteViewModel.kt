package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesfinder.data.model.Favorite
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing favorite recipes.
 *
 * This ViewModel provides functions to save, delete, and retrieve favorite recipes,
 * as well as to obtain related messages and the number of favorites.
 *
 * @property favorite LiveData that holds a single Favorite instance.
 * @property searchMsg LiveData that holds the result code of a search operation.
 * @property listMsg LiveData that holds the result code of a list operation.
 * @property favoriteList LiveData that holds a list of Favorite instances.
 * @property savedMsg LiveData that holds the result code of a save operation.
 * @property numFavorites LiveData that holds the number of favorites.
 */
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favorite = MutableLiveData<Favorite>()
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var favoriteList = MutableLiveData<List<Favorite>>()
    private var savedMsg = MutableLiveData<Int>()
    private val _numFavorites = MutableLiveData<Int>()
    val numFavorites: LiveData<Int> get() = _numFavorites

    /**
     * Returns the LiveData holding a single Favorite.
     *
     * @return LiveData of Favorite.
     */
    fun getFavorite(): LiveData<Favorite> {
        return favorite
    }

    /**
     * Returns the LiveData containing the search message code.
     *
     * @return LiveData of Int representing the search message.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns the LiveData containing the list message code.
     *
     * @return LiveData of Int representing the list message.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns the LiveData holding the list of favorites.
     *
     * @return LiveData of List of Favorite.
     */
    fun getFavoriteList(): LiveData<List<Favorite>> {
        return favoriteList
    }

    /**
     * Returns the LiveData containing the saved message code.
     *
     * @return LiveData of Int representing the saved message.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Saves a new favorite to the database.
     *
     * Inserts the given [Favorite] into the database and updates [savedMsg] with the result.
     *
     * @param u The [Favorite] object to save.
     */
    fun saveUser(u: Favorite) {
        val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
        var resp = 0L
        try {
            resp = db.insert(u)
            savedMsg.value = if (resp > 0) Constants.BD_MSGS.SUCCESS else Constants.BD_MSGS.FAIL
        } catch (e: SQLiteConstraintException) {
            savedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Deletes the given favorite from the database.
     *
     * Executes the delete operation asynchronously. In case of a constraint exception,
     * updates [savedMsg] with the appropriate error code.
     *
     * @param u The [Favorite] object to delete.
     */
    fun deleteFavorite(u: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
            try {
                val resp = db.delete(u)
                withContext(Dispatchers.Main) {
                    // Optionally, you could update savedMsg here if needed
                }
            } catch (e: SQLiteConstraintException) {
                withContext(Dispatchers.Main) {
                    savedMsg.value = Constants.BD_MSGS.CONSTRAINT
                }
            }
        }
    }

    /**
     * Retrieves all favorite recipes from the database.
     *
     * Updates [listMsg] and [favoriteList] based on whether favorites were found or if an error occurred.
     */
    fun getAllFavorites() {
        val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
        try {
            val resp = db.getAllFavorites()
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                favoriteList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves favorites for a specific user.
     *
     * Updates [searchMsg] and [favoriteList] with the results for the given user id.
     *
     * @param id The user identifier.
     */
    fun getByIduser(id: String) {
        val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
        try {
            val resp = db.getByIduser(id)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                favoriteList.value = resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves a specific favorite using user id and recipe id.
     *
     * Updates [searchMsg] and [favorite] based on the result.
     *
     * @param iduser The user identifier.
     * @param idref The recipe identifier.
     */
    fun getById(iduser: String, idref: String) {
        val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
        try {
            val resp = db.getById(iduser, idref)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                favorite.value = resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Updates the LiveData holding the number of favorites for a given recipe.
     *
     * Executes a database query asynchronously to count the number of favorites and updates [_numFavorites].
     *
     * @param recipeId The identifier of the recipe.
     */
    fun updateNumFavorites(recipeId: String) {
        val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
        viewModelScope.launch(Dispatchers.IO) {
            val count = db.getNumFavorite(recipeId)
            withContext(Dispatchers.Main) {
                _numFavorites.value = count
            }
        }
    }

    /**
     * Retrieves the number of favorites for a specific recipe.
     *
     * Executes a synchronous query and updates the corresponding LiveData.
     * Returns "0" as a string. (Note: This method always returns "0" regardless of the actual count.)
     *
     * @param id The identifier of the recipe.
     * @return A string representing the number of favorites (currently always "0").
     */
    fun getNumFavorite(id: String): String {
        val db = AppDatabase.getDatabase(getApplication()).FavoriteDao()
        try {
            val resp = db.getNumFavorite(id)
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _numFavorites.value = resp
                }
            }
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
        return 0.toString()
    }
}
