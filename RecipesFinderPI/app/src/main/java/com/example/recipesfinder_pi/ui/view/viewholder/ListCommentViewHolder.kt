package com.example.recipesfinder.ui.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.data.model.Comment
import com.example.recipesfinder.ui.viewmodel.UserViewModel
import com.example.recipesfinder.databinding.CommentLineBinding
import com.example.recipesfinder.ui.view.listener.OnCommentListener
import com.example.recipesfinder.util.Constants

/**
 * ViewHolder for displaying a comment in a RecyclerView.
 *
 * This ViewHolder binds the comment data to the corresponding UI elements in the comment item layout.
 *
 * @property binding The view binding object for the comment item layout.
 * @property listener The listener for handling comment click events.
 * @property userVM The UserViewModel used to fetch user details based on the comment's user identifier.
 */
class ListCommentViewHolder(
    private val binding: CommentLineBinding,
    private val listener: OnCommentListener,
    private val userVM: UserViewModel
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds a [Comment] object to the ViewHolder.
     *
     * This function populates the UI elements with the comment data, such as the user's nickname,
     * the comment text, and the rating. It also retrieves the user's profile image from a predefined
     * mapping in [Constants.USER_IMAGES] and sets it if available. Additionally, it sets a click listener
     * on the rating view to notify [listener] when the comment is clicked.
     *
     * @param com The [Comment] object containing the details to be displayed.
     */
    fun bindVH(com: Comment) {
        binding.UserName.text = userVM.getByIdComment(com.iduser)?.nickname
        binding.CommentText.text = com.Commentary
        binding.RecipeRating.rating = com.Rating

        val resId = Constants.USER_IMAGES.profileImageMap[userVM.getByIdComment(com.iduser)?.imageURL]
        if (resId != null) {
            binding.UserImage.setImageResource(resId)
        }

        binding.RecipeRating.setOnClickListener {
            listener.onClick(com)
        }
    }
}

