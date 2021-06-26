package com.example.spoc_v2.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.spoc_v2.data.database.entities.RecipesEntity
import com.example.spoc_v2.models.FoodRecipe
import com.example.spoc_v2.util.NetworkResult


class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readRecipes", requireAll = true)
        @JvmStatic
        fun errorImageVieVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextVieVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }
        }
    }
}
/*
            readApiResponse="@{mainViewModel.recipesResponse}"
            readRecipes="@{mainViewModel.readRecipes"

            readApiResponse2="@{mainViewModel.recipesResponse}"
            readDatabase2="@{mainViewModel.readRecipes"
 */