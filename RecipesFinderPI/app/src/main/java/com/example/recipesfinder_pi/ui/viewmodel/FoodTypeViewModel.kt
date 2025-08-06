package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesfinder.data.model.FoodTypeModel
import com.example.recipesfinder.R
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants

/**
 * ViewModel responsible for managing food type data.
 *
 * This ViewModel holds the state and business logic for retrieving and providing
 * food type information to the UI. It exposes LiveData for a single food type,
 * a list of food types, and various status messages regarding search, listing,
 * and saving operations.
 *
 * @constructor Creates a [FoodTypeViewModel] with the given [Application] context.
 *
 * @param application The [Application] context used by the ViewModel.
 */
class FoodTypeViewModel(application: Application) : AndroidViewModel(application) {

    private var foodType = MutableLiveData<FoodTypeModel>()
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var foodTypeList = MutableLiveData<List<FoodTypeModel>>()
    private var savedMsg = MutableLiveData<Int>()

    /**
     * Returns the LiveData containing a single [FoodTypeModel].
     *
     * @return LiveData of [FoodTypeModel].
     */
    fun getFoodType(): LiveData<FoodTypeModel> {
        return foodType
    }

    /**
     * Returns the LiveData representing the status message for search operations.
     *
     * @return LiveData of [Int] indicating the search status.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns the LiveData representing the status message for list operations.
     *
     * @return LiveData of [Int] indicating the list status.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns the LiveData containing the list of [FoodTypeModel].
     *
     * @return LiveData of a list of [FoodTypeModel].
     */
    fun getFoodTypeList(): LiveData<List<FoodTypeModel>> {
        return foodTypeList
    }

    /**
     * Returns the LiveData representing the status message for save operations.
     *
     * @return LiveData of [Int] indicating the save status.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Retrieves all food types and updates the [foodTypeList] LiveData.
     *
     * This method creates a list of [FoodTypeModel] instances with their respective names
     * and associated images. It then checks if the list is not null and updates the LiveData
     * accordingly with a success or not found message. In case of an exception, it updates the
     * LiveData with a failure message.
     */
    fun getAllFoodTypes() {
        try {
            val resp = listOf(
                FoodTypeModel("Japonesa", R.drawable.japonesa),
                FoodTypeModel("Italiana", R.drawable.italiana),
                FoodTypeModel("Mexicana", R.drawable.mexicana),
                FoodTypeModel("Brasileira", R.drawable.brasileira),
                FoodTypeModel("Chinesa", R.drawable.chinesa),
                FoodTypeModel("Indiana", R.drawable.indiana),
                FoodTypeModel("Mediterrânea", R.drawable.mediterranea),
                FoodTypeModel("Francesa", R.drawable.francesa),
                FoodTypeModel("Alemã", R.drawable.alema),
                FoodTypeModel("Americana", R.drawable.lanches),
                FoodTypeModel("Tailandesa", R.drawable.tailandesa),
                FoodTypeModel("Coreana", R.drawable.coreana),
                FoodTypeModel("Árabe", R.drawable.arabe),
                FoodTypeModel("Espanhola", R.drawable.espanhola),
                FoodTypeModel("Vietnamita", R.drawable.vietnamita),
                FoodTypeModel("Caribenha", R.drawable.caribenha),
                FoodTypeModel("Grega", R.drawable.grega),
                FoodTypeModel("Doces", R.drawable.doces),
                FoodTypeModel("Vegetariana", R.drawable.vegetariana),
                FoodTypeModel("Low Carb", R.drawable.low_carb)
            )
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                foodTypeList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }
}
