package com.example.recipesfinder.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesfinder.data.model.Comment
import com.example.recipesfinder.data.local.room.AppDatabase
import com.example.recipesfinder.util.Constants

/**
 * ViewModel responsible for managing Comment data in the RecipesFinder app.
 *
 * This ViewModel provides LiveData for a single comment, a list of comments,
 * and messages indicating the result of database operations (such as saving or retrieving comments).
 *
 * @param application The Application context used to access the database.
 */
class CommentViewModel(application: Application) : AndroidViewModel(application) {

    private var comment = MutableLiveData<Comment>()
    private var AVG = Float // Note: This variable is declared but never used.
    private var searchMsg = MutableLiveData<Int>()
    private var listMsg = MutableLiveData<Int>()
    private var commentList = MutableLiveData<List<Comment>>()
    private var savedMsg = MutableLiveData<Int>()

    /**
     * Returns a LiveData containing a single Comment.
     *
     * @return LiveData<Comment> representing a comment.
     */
    fun getComment(): LiveData<Comment> {
        return comment
    }

    /**
     * Returns a LiveData containing the message code for search operations.
     *
     * @return LiveData<Int> representing the status message after a search.
     */
    fun getIsSearched(): LiveData<Int> {
        return searchMsg
    }

    /**
     * Returns a LiveData containing the message code for list retrieval operations.
     *
     * @return LiveData<Int> representing the status message after listing comments.
     */
    fun getIsListed(): LiveData<Int> {
        return listMsg
    }

    /**
     * Returns a LiveData containing a list of Comment objects.
     *
     * @return LiveData<List<Comment>> representing the list of comments.
     */
    fun getCommentList(): LiveData<List<Comment>> {
        return commentList
    }

    /**
     * Returns a LiveData containing the message code for save operations.
     *
     * @return LiveData<Int> representing the status message after saving a comment.
     */
    fun getIsSaved(): LiveData<Int> {
        return savedMsg
    }

    /**
     * Saves the provided comment to the database.
     *
     * Updates [savedMsg] LiveData with:
     * - [Constants.BD_MSGS.SUCCESS] if the insert returns a positive id,
     * - [Constants.BD_MSGS.FAIL] if the insert fails,
     * - [Constants.BD_MSGS.CONSTRAINT] if a SQLiteConstraintException is thrown.
     *
     * @param c The [Comment] to be saved.
     */
    fun saveComment(c: Comment) {
        val db = AppDatabase.getDatabase(getApplication()).CommentDao()
        var resp = 0L
        try {
            resp = db.insert(c)
            savedMsg.value = if (resp > 0) Constants.BD_MSGS.SUCCESS else Constants.BD_MSGS.FAIL
        } catch (e: SQLiteConstraintException) {
            savedMsg.value = Constants.BD_MSGS.CONSTRAINT
        }
    }

    /**
     * Retrieves all comments from the database and updates [commentList] and [listMsg] accordingly.
     *
     * If no comments are found, [listMsg] is updated with [Constants.BD_MSGS.NOT_FOUND],
     * otherwise with [Constants.BD_MSGS.SUCCESS]. In case of an exception, [listMsg] is set to [Constants.BD_MSGS.FAIL].
     */
    fun getAllComments() {
        val db = AppDatabase.getDatabase(getApplication()).CommentDao()
        try {
            val resp = db.getAllComments()
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                commentList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves comments by the specified user id from the database.
     *
     * Updates [searchMsg] with [Constants.BD_MSGS.NOT_FOUND] if no comments are found,
     * or [Constants.BD_MSGS.SUCCESS] if comments are retrieved successfully.
     * In case of an exception, [searchMsg] is set to [Constants.BD_MSGS.FAIL].
     *
     * @param id The identifier of the user whose comments are to be retrieved.
     */
    fun getByIduser(id: String) {
        val db = AppDatabase.getDatabase(getApplication()).CommentDao()
        try {
            val resp = db.getByIduser(id)
            if (resp == null) {
                searchMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                searchMsg.value = Constants.BD_MSGS.SUCCESS
                commentList.value = resp
            }
        } catch (e: Exception) {
            searchMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Retrieves comments by the specified recipe id from the database.
     *
     * Updates [listMsg] with [Constants.BD_MSGS.NOT_FOUND] if no comments are found,
     * or [Constants.BD_MSGS.SUCCESS] if comments are retrieved successfully.
     * In case of an exception, [listMsg] is set to [Constants.BD_MSGS.FAIL].
     *
     * @param id The recipe identifier (idref) for which comments are to be retrieved.
     */
    fun getByIdref(id: String) {
        val db = AppDatabase.getDatabase(getApplication()).CommentDao()
        try {
            val resp = db.getByIdref(id)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                commentList.value = resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
    }

    /**
     * Calculates and returns the average rating for comments associated with the given recipe id.
     *
     * If no ratings are found, the method updates [listMsg] with [Constants.BD_MSGS.NOT_FOUND]
     * and returns 0F. If comments are retrieved successfully, [listMsg] is set to [Constants.BD_MSGS.SUCCESS].
     * In case of an exception, [listMsg] is updated with [Constants.BD_MSGS.FAIL].
     *
     * @param id The recipe identifier (idref) for which the average rating is to be calculated.
     * @return The average rating as a [Float]. Returns 0F if no rating is found or an error occurs.
     */
    fun getAverageRating(id: String): Float {
        val db = AppDatabase.getDatabase(getApplication()).CommentDao()
        try {
            val resp = db.getAverageRating(id)
            if (resp == null) {
                listMsg.value = Constants.BD_MSGS.NOT_FOUND
            } else {
                listMsg.value = Constants.BD_MSGS.SUCCESS
                return resp
            }
        } catch (e: Exception) {
            listMsg.value = Constants.BD_MSGS.FAIL
        }
        return 0F
    }
}