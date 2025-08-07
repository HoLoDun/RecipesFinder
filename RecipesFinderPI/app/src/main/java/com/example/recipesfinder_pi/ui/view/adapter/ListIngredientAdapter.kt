package com.example.recipesfinder.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.IngredientLineBinding
import com.example.recipesfinder.ui.view.viewholder.ListIngredientViewHolder
import com.example.recipesfinder.data.model.UsedIngredient
import com.example.recipesfinder.ui.viewmodel.IngredientViewModel


/**
 * Adapter for displaying a list of used ingredients in a RecyclerView.
 *
 * This adapter binds a list of [UsedIngredient] items to the view holders and uses an instance
 * of [IngredientViewModel] to provide necessary data.
 */
class ListIngredientAdapter : RecyclerView.Adapter<ListIngredientViewHolder>() {

    // List of used ingredients to be displayed.
    private var reclist: List<UsedIngredient> = listOf()

    // ViewModel instance to manage ingredient data.
    lateinit var ingvm: IngredientViewModel

    /**
     * Called when RecyclerView needs a new [ListIngredientViewHolder] of the given type
     * to represent an item.
     *
     * @param parent The parent ViewGroup that will hold the view.
     * @param viewType The view type of the new View.
     * @return A new instance of [ListIngredientViewHolder] that holds the inflated view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListIngredientViewHolder {
        val item = IngredientLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListIngredientViewHolder(item, ingvm)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * This method updates the contents of the [ListIngredientViewHolder] to reflect the item
     * at the given position in the data set.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ListIngredientViewHolder, position: Int) {
        holder.bindVH(reclist[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int {
        return reclist.size
    }

    /**
     * Updates the list of used ingredients and notifies the adapter that the data set has changed.
     *
     * @param list The new list of [UsedIngredient] items.
     */
    fun updateRecipeList(list: List<UsedIngredient>) {
        reclist = list
        notifyDataSetChanged()
    }
}
