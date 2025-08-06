package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants

/**
 * ViewModel for managing recipe data.
 *
 * This ViewModel handles all operations related to recipes, including saving a new recipe,
 * retrieving recipes by various queries, and maintaining status messages and LiveData objects
 * for the current recipe and list of recipes. Status messages are defined in [Constants.BD_MSGS]
 * and indicate success, failure, or not found conditions.
 *
 * @constructor Creates a RecipeViewModel with the given application context.
 *
 * @param application The Application context.
 */
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private var recipe = MutableLiveData<Recipe>()
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var recipeList = MutableLiveData<List<Recipe>>()
    private var savedMsg = MutableLiveData<Int>()

    /**
     * Returns a LiveData object representing the current recipe.
     *
     * @return A [LiveData] containing the current [Recipe].
     */
    fun getRecipe(): LiveData<Recipe> {
        return recipe
    }

    /**
     * Returns a LiveData object representing the status of a search operation.
     *
     * The status is defined by [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for search operations.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns a LiveData object representing the status of a list operation.
     *
     * The status is defined by [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for list operations.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns a LiveData object containing a list of recipes.
     *
     * @return A [LiveData] containing a list of [Recipe] objects.
     */
    fun getRecipeList(): LiveData<List<Recipe>> {
        return recipeList
    }

    /**
     * Returns a LiveData object representing the status of a save operation.
     *
     * The status is defined by [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for save operations.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Saves the given recipe into the database.
     *
     * This function attempts to insert a new recipe into the database. Upon completion, it updates
     * the [savedMsg] LiveData with the status of the operation (SUCCESS, FAIL, or CONSTRAINT error).
     * After saving, it queries the recipe by name and returns its ID as a string.
     *
     * @param r The [Recipe] to be saved.
     * @return The ID of the saved recipe as a [String].
     */
    fun saveRecipe(r: Recipe) : String {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        var resp = 0L
        try {
            resp = db.insert(r)
            savedMsg.value = if (resp > 0) Constants.BD_MSGS.SUCCESS else Constants.BD_MSGS.FAIL
        } catch (e: SQLiteConstraintException) {
            savedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
        getByName(r.name)
        if (listMsg.value == Constants.BD_MSGS.SUCCESS) {
            return recipe.value?.id.toString()
        }
        return recipe.value?.id.toString()
    }

    /**
     * Retrieves all recipes from the database.
     *
     * This function queries the database for all recipes. It updates [recipeList] with the result and
     * sets [listMsg] to indicate whether the operation was successful, not found, or failed.
     */
    fun getAllRecipes() {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        try {
            val resp = db.getAllRecipes()
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves a recipe by its unique ID.
     *
     * This function queries the database using the recipe's ID. If found, it updates [recipe] and
     * [searchMsg] accordingly.
     *
     * @param id The unique identifier of the recipe.
     */
    fun getById(id: Long) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        try {
            val resp = db.getById(id)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                recipe.value = resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves recipes associated with the given user ID.
     *
     * This function updates [recipeList] and [listMsg] based on whether recipes for the user are found.
     *
     * @param iduser The user ID for which recipes are to be retrieved.
     */
    fun getByIduser(iduser: String) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        try {
            val resp = db.getByIduser(iduser)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves recipes marked as favorites for the specified user.
     *
     * This function updates [recipeList] and [listMsg] based on whether favorite recipes for the user are found.
     *
     * @param iduser The user ID whose favorite recipes are to be retrieved.
     */
    fun getByIduserFavorites(iduser: String) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        try {
            val resp = db.getByIduserFavorites(iduser)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves recipes based on a specific type.
     *
     * This function queries the database by the recipe type, updating [recipeList] and [listMsg]
     * accordingly.
     *
     * @param type The type of recipe to search for.
     */
    fun getByType(type: String) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        try {
            val resp = db.getByType(type)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves a recipe by its name.
     *
     * This function queries the database using the recipe name. If found, it updates [recipe] and
     * [listMsg] accordingly.
     *
     * @param name The name of the recipe.
     */
    fun getByName(name: String) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        try {
            val resp = db.getByName(name)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipe.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves a recipe by a query name.
     *
     * If the provided name is an empty string, all recipes are retrieved.
     * Otherwise, the function queries the database using the query name, updating [recipe] and [listMsg]
     * with the result.
     *
     * @param name The query string to search for in recipe names.
     */
    fun getByQName(name: String) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        if (name == "") {
            getAllRecipes()
            return
        }
        try {
            val resp = db.getByQName(name)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
                recipe.value = resp
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipe.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves recipes based on multiple query parameters.
     *
     * This function searches recipes by name, type, calories, and ingredients. The ingredients list is first
     * processed by [searchIngredients] to match ingredients in the database. The result updates [recipeList]
     * and [listMsg] to indicate the success or failure of the query.
     *
     * @param name The name query for recipes.
     * @param type A list of recipe types to filter.
     * @param calories The minimum calories filter.
     * @param ingredients A list of ingredients to search for.
     * @param typesize The size of the type list.
     * @param ingredientsSize The size of the ingredients list.
     */
    fun getByQuery(
        name: String,
        type: List<String> = emptyList(),
        calories: Int = 0,
        ingredients: List<String> = emptyList(),
        typesize: Int = 0,
        ingredientsSize: Int = 0
    ) {
        val db = AppDatabase.getDatabase(getApplication()).RecipeDao()
        val ingredientslist = searchIngredients(ingredients)
        try {
            val resp = db.getByQuery(name, type, calories, ingredientslist, typesize, ingredientslist.size)
            if (resp.isNullOrEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
                recipeList.value = resp
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                recipeList.value = resp
            }
        } catch (e: Exception) {
            Log.e("Query", "Erro ao buscar Query", e)
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Searches for ingredients matching the provided list of ingredient strings.
     *
     * This function iterates over the list of ingredient strings, queries the database for matching ingredients,
     * and aggregates their names into a single list. The status message [listMsg] is updated accordingly.
     *
     * @param ingredients A list of ingredient strings to search for.
     * @return A [List] of ingredient names matching the search criteria.
     */
    fun searchIngredients(ingredients: List<String> = emptyList()): List<String> {
        val dbing = AppDatabase.getDatabase(getApplication()).IngredientDao()
        val resp = mutableListOf<String>()
        try {
            for (i in ingredients.indices) {
                val ingredient = ingredients[i]
                val inglist = dbing.searchIngredients(ingredient)
                for (ing in inglist) {
                    resp.add(ing.name)
                }
            }
            if (resp.isNullOrEmpty()) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
                return resp
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                return resp
            }
        } catch (e: Exception) {
            Log.e("Query", "Erro ao buscar Query", e)
            listMsg.value = Constants.BD_MSGS.FAIL
        }
        return emptyList()
    }
}
