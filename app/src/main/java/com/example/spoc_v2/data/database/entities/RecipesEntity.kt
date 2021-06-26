package com.example.spoc_v2.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spoc_v2.models.FoodRecipe
import com.example.spoc_v2.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    //Added to fix
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}