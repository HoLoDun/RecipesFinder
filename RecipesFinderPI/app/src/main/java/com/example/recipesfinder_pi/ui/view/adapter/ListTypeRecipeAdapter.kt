package com.example.recipesfinder.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesfinder.databinding.TypeRecipeLineBinding
import com.example.recipesfinder.ui.view.listener.ListTypeRecipeListener
import com.example.recipesfinder.ui.view.viewholder.FoodTypeViewHolder
import com.example.recipesfinder.data.model.FoodTypeModel
import com.example.recipesfinder.data.model.Recipe

/**
 * Adapter for displaying a list of food types in a RecyclerView.
 *
 * This adapter binds a list of [FoodTypeModel] objects to [FoodTypeViewHolder] instances,
 * allowing for the display of food type items in a list. It also supports setting a listener
 * to handle item click events.
 */
class ListTypeRecipeAdapter : RecyclerView.Adapter<FoodTypeViewHolder>() {

    private var list: List<FoodTypeModel> = listOf()
    private lateinit var listener: ListTypeRecipeListener

    /**
     * Called when RecyclerView needs a new [FoodTypeViewHolder] of the given type to represent an item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of [FoodTypeViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypeViewHolder {
        val item = TypeRecipeLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodTypeViewHolder(item, listener)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * This method binds the [FoodTypeModel] at the given position to the provided [FoodTypeViewHolder].
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: FoodTypeViewHolder, position: Int) {
        holder.bindVH(list[position])
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in [list].
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Updates the adapter's list of food types and notifies that the data set has changed.
     *
     * @param list The new list of [FoodTypeModel] items.
     */
    fun updateFoodTypeList(list: List<FoodTypeModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    /**
     * Sets the listener for item interactions.
     *
     * @param productListener The [ListTypeRecipeListener] that will handle item click events.
     */
    fun setListener(productListener: ListTypeRecipeListener) {
        listener = productListener
    }
}
