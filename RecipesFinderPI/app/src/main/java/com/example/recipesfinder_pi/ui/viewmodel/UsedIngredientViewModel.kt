package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesfinder.data.model.UsedIngredient
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants

/**
 * ViewModel for managing used ingredients data.
 *
 * This ViewModel is responsible for handling operations related to used ingredients,
 * such as saving a used ingredient, retrieving all used ingredients, and querying by
 * ingredient ID or recipe reference. It exposes several LiveData objects for the current
 * used ingredient, a list of used ingredients, and status messages for search, list, and save operations.
 *
 * Status messages are defined in [Constants.BD_MSGS] and indicate success, failure, or not found conditions.
 *
 * @constructor Creates a UsedIngredientViewModel with the given application context.
 *
 * @param application The Application context.
 */
class UsedIngredientViewModel(application: Application) : AndroidViewModel(application) {

    private var favorite = MutableLiveData<UsedIngredient>()
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var favoriteList = MutableLiveData<List<UsedIngredient>>()
    private var savedMsg = MutableLiveData<Int>()

    /**
     * Returns the LiveData object representing the current used ingredient.
     *
     * @return A [LiveData] containing the current [UsedIngredient].
     */
    fun getUsedIngredient(): LiveData<UsedIngredient> {
        return favorite
    }

    /**
     * Returns the LiveData object representing the search status message.
     *
     * The status is defined in [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for search operations.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns the LiveData object representing the list status message.
     *
     * The status is defined in [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for list operations.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns the LiveData object containing the list of used ingredients.
     *
     * @return A [LiveData] containing a list of [UsedIngredient] objects.
     */
    fun getUsedIngredientList(): LiveData<List<UsedIngredient>> {
        return favoriteList
    }

    /**
     * Returns the LiveData object representing the save operation status.
     *
     * The status is defined in [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for save operations.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Saves the given used ingredient in the database.
     *
     * This method attempts to insert a new used ingredient into the database and updates
     * the [savedMsg] LiveData with the operation status. If a [SQLiteConstraintException]
     * occurs, the save status is set to [Constants.BD_MSGS.CONSTRAINT].
     *
     * @param u The [UsedIngredient] to be saved.
     */
    fun saveUser(u: UsedIngredient) {
        val db = AppDatabase.getDatabase(getApplication()).UsedIngredientDao()
        var resp = 0L
        try {
            resp = db.insert(u)
            savedMsg.value = if (resp > 0) Constants.BD_MSGS.SUCCESS else Constants.BD_MSGS.FAIL
        } catch (e: SQLiteConstraintException) {
            savedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Retrieves all used ingredients from the database.
     *
     * This method updates [favoriteList] with the retrieved list and sets [listMsg]
     * to indicate whether the operation was successful, not found, or failed.
     */
    fun getAllUsers() {
        val db = AppDatabase.getDatabase(getApplication()).UsedIngredientDao()
        try {
            val resp = db.getAllUsedIngredients()
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
     * Retrieves used ingredients by ingredient ID.
     *
     * This method queries the database using the provided ingredient ID and updates
     * [favoriteList] and [searchMsg] based on the result.
     *
     * @param id The identifier of the ingredient to query.
     */
    fun getByIding(id: String) {
        val db = AppDatabase.getDatabase(getApplication()).UsedIngredientDao()
        try {
            val resp = db.getByIding(id)
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
     * Retrieves used ingredients by recipe ID.
     *
     * This method queries the database using the provided recipe reference ID and updates
     * [favoriteList] and [searchMsg] based on the result.
     *
     * @param id The reference ID of the recipe.
     */
    fun getByIdref(id: String) {
        val db = AppDatabase.getDatabase(getApplication()).UsedIngredientDao()
        try {
            val resp = db.getByIdref(id)
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
}
