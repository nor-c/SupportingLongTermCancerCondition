package com.example.spoc_v2.data.database

import androidx.room.TypeConverter
import com.example.spoc_v2.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.spoc_v2.models.Result

class RecipesTypeConvener {

    var gson = Gson()

    @TypeConverter
    fun fooRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): FoodRecipe{
        val listType = object: TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data, listType)
    }
    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }
    @TypeConverter
    fun stringToResult(data: String): Result{
        val listType = object: TypeToken<Result>(){}.type
        return gson.fromJson(data, listType)
    }
}