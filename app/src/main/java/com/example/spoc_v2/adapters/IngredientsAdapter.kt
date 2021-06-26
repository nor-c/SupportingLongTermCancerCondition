package com.example.spoc_v2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spoc_v2.R
import com.example.spoc_v2.models.ExtendedIngredient

import com.example.spoc_v2.util.Constants.Companion.BASE_IMAGE_URL
import com.example.spoc_v2.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredians_row_layout.view.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredians_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientsAdapter.MyViewHolder, position: Int) {

        holder.itemView.ingredients_imageView.load(BASE_IMAGE_URL + ingredientsList[position].image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.itemView.ingredient_name.text = ingredientsList[position].name.capitalize()
        holder.itemView.ingredient_amount.text = ingredientsList[position].amount.toString()
        holder.itemView.ingredient_unit.text = ingredientsList[position].unit
        holder.itemView.ingredient_consistency.text = ingredientsList[position].consistency
        holder.itemView.ingredient_original.text = ingredientsList[position].original
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredient: List<ExtendedIngredient>){
        val ingredientsDiffUnit = RecipesDiffUtil(ingredientsList, newIngredient )
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUnit)
        ingredientsList = newIngredient
        diffUtilResult.dispatchUpdatesTo(this)
    }
}