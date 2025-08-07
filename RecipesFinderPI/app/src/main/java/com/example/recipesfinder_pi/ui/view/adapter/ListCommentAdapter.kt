package com.example.recipesfinder.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.CommentLineBinding
import com.example.recipesfinder.ui.viewmodel.UserViewModel
import com.example.recipesfinder.ui.view.listener.OnCommentListener
import com.example.recipesfinder.ui.view.viewholder.ListCommentViewHolder
import com.example.recipesfinder.data.model.Comment

/**
 * Adapter responsible for displaying a list of comments in a RecyclerView.
 *
 * This adapter binds [Comment] objects to [ListCommentViewHolder] instances,
 * allowing the display and interaction with a list of comments.
 *
 * @property comlist The list of comments to be displayed.
 * @property listener The listener to handle click events on a comment item.
 */
class ListCommentAdapter : RecyclerView.Adapter<ListCommentViewHolder>() {

    private var comlist: List<Comment> = listOf()
    private var listener: OnCommentListener = object : OnCommentListener {
        override fun onClick(c: Comment) {
            // No-op: default empty implementation
        }
    }

    /**
     * Called when RecyclerView needs a new [ListCommentViewHolder] of the given type to represent an item.
     *
     * Inflates the [CommentLineBinding] layout and obtains a [UserViewModel] from the parent context.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of [ListCommentViewHolder] that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCommentViewHolder {
        val item = CommentLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val uservm = ViewModelProvider(parent.context as AppCompatActivity).get(UserViewModel::class.java)
        return ListCommentViewHolder(item, listener, uservm)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * Binds the [Comment] from the comment list at the given position to the provided [ListCommentViewHolder].
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ListCommentViewHolder, position: Int) {
        holder.bindVH(comlist[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The size of the comment list.
     */
    override fun getItemCount(): Int {
        return comlist.size
    }

    /**
     * Updates the adapter's comment list with a new list of comments and refreshes the RecyclerView.
     *
     * @param list A new list of [Comment] objects.
     */
    fun updateCommentList(list: List<Comment>) {
        comlist = list
        notifyDataSetChanged()
    }

    /**
     * Sets a listener to handle comment click events.
     *
     * @param productListener An implementation of [OnCommentListener] to be notified when a comment is clicked.
     */
    fun setListener(productListener: OnCommentListener) {
        listener = productListener
    }
}
