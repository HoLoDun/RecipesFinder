package com.example.recipesfinder.ui.view.listener

import com.example.recipesfinder.data.model.Comment

/**
 * Listener interface for handling click events on a comment.
 *
 * Implement this interface to receive callbacks when a comment is clicked.
 */
interface OnCommentListener {
    /**
     * Invoked when a comment is clicked.
     *
     * @param food The [Comment] instance that was clicked.
     */
    fun onClick(food: Comment)
}
