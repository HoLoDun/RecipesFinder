package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.example.recipesfinder.data.model.User
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants

/**
 * ViewModel for managing user data.
 *
 * This ViewModel handles operations related to users, such as saving a new user,
 * retrieving user data by email, ID, or listing all users, and updating the user's profile image.
 * It exposes several LiveData objects for a single user, a list of users, and status messages for
 * search, listing, and save operations. Status messages are defined in [Constants.BD_MSGS].
 *
 * @constructor Creates a UserViewModel with the given application context.
 *
 * @param application The Application context.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var userobj = MutableLiveData<User>()
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var userList = MutableLiveData<List<User>>()
    private var savedMsg = MutableLiveData<Int>()

    /**
     * Returns a LiveData object representing the current user.
     *
     * @return A [LiveData] containing the current [User].
     */
    fun getUser(): LiveData<User> {
        return userobj
    }

    /**
     * Returns a LiveData object representing the search status message.
     *
     * The status is defined by [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for search operations.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns a LiveData object representing the list status message.
     *
     * The status is defined by [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for list operations.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns a LiveData object containing the list of users.
     *
     * @return A [LiveData] containing a list of [User] objects.
     */
    fun getUserList(): LiveData<List<User>> {
        return userList
    }

    /**
     * Returns a LiveData object representing the save operation status.
     *
     * The status is defined by [Constants.BD_MSGS].
     *
     * @return A [LiveData] containing an integer status for save operations.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Saves the provided user to the database.
     *
     * This method attempts to insert a new user into the database and updates the [savedMsg]
     * LiveData with the operation status (SUCCESS, FAIL, or CONSTRAINT error).
     *
     * @param u The [User] to be saved.
     */
    fun saveUser(u: User) {
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        var resp = 0L
        try {
            resp = db.insert(u)
            savedMsg.value = if (resp > 0) Constants.BD_MSGS.SUCCESS else Constants.BD_MSGS.FAIL
        } catch (e: SQLiteConstraintException) {
            savedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Retrieves user data by email.
     *
     * This method queries the database using the user's email and updates [userobj] and [listMsg]
     * with the result. It returns the ID of the retrieved user as a String.
     *
     * @return The user ID as a [String] if found, or an empty string otherwise.
     */
    fun getUserData(): String {
        var email = ""
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        try {
            val resp = db.getByEmail(email)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                userobj.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
        return userobj.value?.id.toString()
    }

    /**
     * Retrieves all users from the database.
     *
     * This method updates [userList] with the retrieved list of users and sets [listMsg]
     * to indicate whether the operation was successful, not found, or failed.
     */
    fun getAllUsers() {
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        try {
            val resp = db.getAllUsers()
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                userList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * This method queries the database using the provided ID and updates [userobj] and [searchMsg]
     * based on the result.
     *
     * @param id The unique identifier of the user.
     */
    fun getById(id: String) {
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        try {
            val resp = db.getById(id)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                userobj.value = resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves a user by their unique ID saved on a comment object.
     *
     * This method functions similarly to [getById], but returns the [User] if found.
     *
     * @param id The unique identifier of the user.
     * @return The [User] if found; otherwise, null.
     */
    fun getByIdComment(id: String): User? {
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        try {
            val resp = db.getById(id)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                userobj.value = resp
                return resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
        return null
    }

    /**
     * Retrieves a user by their email.
     *
     * This method queries the database using the provided email and updates [userobj],
     * [searchMsg], and [listMsg] based on the result.
     *
     * @param email The email address of the user.
     */
    fun getByEmail(email: String) {
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        try {
            val resp = db.getByEmail(email)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                userobj.value = resp
            }
        } catch (e: Exception) {
            Log.e("getByEmail", "Erro ao buscar usuário", e)
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Updates the profile image for the specified user.
     *
     * This method updates the user's profile image in the database. If successful,
     * [searchMsg] is set to SUCCESS; otherwise, it is set to FAIL.
     *
     * @param userId The unique identifier of the user.
     * @param profilePicture The new profile picture URL or identifier.
     */
    fun updateUserImage(userId: String, profilePicture: String) {
        val db = AppDatabase.getDatabase(getApplication()).UserDao()
        try {
            val resp = db.updateUserImage(userId, profilePicture)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "Erro ao atualizar imagem", e)
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }
}
