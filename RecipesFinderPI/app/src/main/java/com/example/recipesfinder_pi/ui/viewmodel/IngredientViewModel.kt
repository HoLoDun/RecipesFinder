package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesfinder.data.model.Ingredient
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants

/**
 * ViewModel for managing ingredient data.
 *
 * This ViewModel encapsulates the business logic for querying, saving, and retrieving ingredients
 * from the local database. It exposes several LiveData objects that represent the current ingredient,
 * a list of ingredients, and status messages for search, listing, and save operations.
 *
 * @constructor Creates an IngredientViewModel with the provided application context.
 *
 * @param application The Application context.
 */
class IngredientViewModel(application: Application) : AndroidViewModel(application) {

    private var ingredient = MutableLiveData<Ingredient>()
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var ingredientList = MutableLiveData<List<Ingredient>>()
    private var savedMsg = MutableLiveData<Int>()

    /**
     * Returns the LiveData object that holds the current ingredient.
     *
     * @return A [LiveData] containing the current [Ingredient].
     */
    fun getIngredient(): LiveData<Ingredient> {
        return ingredient
    }

    /**
     * Returns the LiveData object that holds the search status message.
     *
     * The status is represented as an integer defined in [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing the search status.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns the LiveData object that holds the listing status message.
     *
     * The status is represented as an integer defined in [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing the list status.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns the LiveData object that holds the list of ingredients.
     *
     * @return A [LiveData] containing a list of [Ingredient] objects.
     */
    fun getIngredientList(): LiveData<List<Ingredient>> {
        return ingredientList
    }

    /**
     * Returns the LiveData object that holds the status of a save operation.
     *
     * The status is represented as an integer defined in [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing the save status.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Attempts to save the given ingredient into the database.
     *
     * If the insertion is successful (i.e., a positive ID is returned), the [savedMsg] LiveData
     * is set to [Constants.BD_MSGS.SUCCESS]. Otherwise, it is set to [Constants.BD_MSGS.FAIL].
     * If a [SQLiteConstraintException] is caught, the [savedMsg] is set to [Constants.BD_MSGS.CONSTRAINT].
     *
     * @param u The [Ingredient] to be saved.
     */
    fun saveIngredient(u: Ingredient) {
        val db = AppDatabase.getDatabase(getApplication()).IngredientDao()
        var resp = 0L
        try {
            resp = db.insert(u)
            savedMsg.value = if (resp > 0) Constants.BD_MSGS.SUCCESS else Constants.BD_MSGS.FAIL
        } catch (e: SQLiteConstraintException) {
            savedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Checks if an ingredient with the given name exists; if not, saves the ingredient.
     *
     * This function performs a query using the ingredient's name. If the ingredient is not found,
     * it saves the ingredient and queries again to update the LiveData. Finally, it returns the
     * ID of the ingredient as a string.
     *
     * @param u The [Ingredient] to check and possibly save.
     * @return The ID of the ingredient as a [String].
     */
    fun checkAndSaveIngredient(u: Ingredient): String {
        getByQuery(u.name)
        if (searchMsg.value == Constants.BD_MSGS.NOT_FOUND) {
            saveIngredient(u)
            getByQuery(u.name)
            return ingredient.value?.id.toString()
        }
        return ingredient.value?.id.toString()
    }

    /**
     * Retrieves all ingredients from the database.
     *
     * This function updates [ingredientList] with the retrieved list and sets [listMsg] to indicate
     * success, not found, or failure.
     */
    fun getAllIngredients() {
        val db = AppDatabase.getDatabase(getApplication()).IngredientDao()
        try {
            val resp = db.getAllIngredients()
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                ingredientList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves an ingredient by its ID from the database.
     *
     * This function updates [searchMsg] and [ingredient] based on the query result.
     * If found, it returns the ingredient's name; otherwise, it returns an empty string.
     *
     * @param query The ID of the ingredient to be queried.
     * @return The name of the ingredient if found; an empty [String] otherwise.
     */
    fun getByid(query: String): String {
        val db = AppDatabase.getDatabase(getApplication()).IngredientDao()
        try {
            val resp = db.getByid(query)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                ingredient.value = resp
                return resp.name
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
        return ""
    }

    /**
     * Queries the database for an ingredient matching the provided query string.
     *
     * This function updates [searchMsg] and [ingredient] based on whether an ingredient matching
     * the query is found or not.
     *
     * @param query The search query to match against ingredient names.
     */
    fun getByQuery(query: String) {
        val db = AppDatabase.getDatabase(getApplication()).IngredientDao()
        try {
            val resp = db.getByQuery(query)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                ingredient.value = resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
    }
}

