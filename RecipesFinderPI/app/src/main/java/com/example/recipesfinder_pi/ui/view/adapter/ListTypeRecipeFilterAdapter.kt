package com.example.recipesfinder.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.TypeRecipeFilterLineBinding
import com.example.recipesfinder.ui.view.listener.ListTypeRecipeListener
import com.example.recipesfinder.ui.view.viewholder.FoodTypeFilterViewHolder
import com.example.recipesfinder.data.model.FoodTypeModel
import com.example.recipesfinder.data.model.Recipe

/**
 * RecyclerView Adapter for displaying a list of food type in a RecyclerView for the  fragment of the search filter.
 *
 * This adapter binds a list of [FoodTypeModel] items to [FoodTypeFilterViewHolder] instances.
 * It also manages a listener to handle click events on each item.
 *
 * @property list A list of [FoodTypeModel] representing the available food type filters.
 * @property listener A [ListTypeRecipeListener] to handle interactions with the items.
 */
class ListTypeRecipeFilterAdapter : RecyclerView.Adapter<FoodTypeFilterViewHolder>() {

    private var list: List<FoodTypeModel> = listOf()
    private lateinit var listener: ListTypeRecipeListener

    /**
     * Called when RecyclerView needs a new [FoodTypeFilterViewHolder] of the given type to represent an item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new instance of [FoodTypeFilterViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypeFilterViewHolder {
        val item = TypeRecipeFilterLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodTypeFilterViewHolder(item, listener)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The [FoodTypeFilterViewHolder] which should be updated to represent the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: FoodTypeFilterViewHolder, position: Int) {
        holder.bindVH(list[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The size of the list.
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Updates the adapter's list of food type filters.
     *
     * @param list A new list of [FoodTypeModel] items to display.
     */
    fun updateFoodTypeList(list: List<FoodTypeModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    /**
     * Sets the listener for item click events.
     *
     * @param productListener A [ListTypeRecipeListener] that will handle item clicks.
     */
    fun setListener(productListener: ListTypeRecipeListener) {
        listener = productListener
    }
}

