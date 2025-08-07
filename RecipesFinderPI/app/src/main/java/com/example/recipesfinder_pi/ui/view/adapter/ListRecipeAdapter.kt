package com.example.recipesfinder.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.RecipeLineBinding
import com.example.recipesfinder.ui.view.listener.OnRecipeListerner
import com.example.recipesfinder.ui.view.viewholder.ListRecipeViewHolder
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.ui.viewmodel.CommentViewModel

/**
 * Adapter for displaying a list of recipes in a RecyclerView.
 *
 * This adapter manages a list of recipes and binds each recipe to a [ListRecipeViewHolder]. It also
 * supports updating the recipe list and handling click events via an [OnRecipeListerner].
 */
class ListRecipeAdapter : RecyclerView.Adapter<ListRecipeViewHolder>() {

    // The list of recipes to display.
    private var reclist: List<Recipe> = listOf()

    // Listener for recipe click events. Initialized with an empty implementation.
    private var listener: OnRecipeListerner = object : OnRecipeListerner {
        override fun onClick(p: Recipe) {
            // Empty implementation.
        }
    }

    /**
     * Creates a new [ListRecipeViewHolder] instance.
     *
     * Inflates the item layout using [RecipeLineBinding] and obtains a [CommentViewModel] from the
     * activity's [ViewModelProvider]. This view holder will be used to display a single recipe.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view (not used in this adapter).
     * @return A new [ListRecipeViewHolder] instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRecipeViewHolder {
        val item = RecipeLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val commentVM = ViewModelProvider(parent.context as AppCompatActivity).get(CommentViewModel::class.java)
        return ListRecipeViewHolder(item, listener, commentVM)
    }

    /**
     * Binds the recipe data at the specified position to the given [ListRecipeViewHolder].
     *
     * @param holder The [ListRecipeViewHolder] which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ListRecipeViewHolder, position: Int) {
        holder.bindVH(reclist[position])
    }

    /**
     * Returns the total number of items (recipes) in the adapter.
     *
     * @return The size of the recipe list.
     */
    override fun getItemCount(): Int {
        return reclist.size
    }

    /**
     * Updates the adapter with a new list of recipes.
     *
     * Logs the new list size and notifies the adapter of the data change on the main thread.
     * If called from a background thread, posts the notification to the main thread.
     *
     * @param list The new list of recipes to display.
     */
    fun updateRecipeList(list: List<Recipe>) {
        Log.d("ListRecipeAdapter", "updateRecipeList called with list size: ${list.size}")
        reclist = list

        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            Log.d("ListRecipeAdapter", "Calling notifyDataSetChanged() on main thread")
            notifyDataSetChanged()
        } else {
            Log.d("ListRecipeAdapter", "Not on main thread, posting notifyDataSetChanged()")
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Sets the click listener for recipe items.
     *
     * @param productListener The [OnRecipeListerner] implementation that will handle click events.
     */
    fun setListener(productListener: OnRecipeListerner) {
        listener = productListener
    }
}
