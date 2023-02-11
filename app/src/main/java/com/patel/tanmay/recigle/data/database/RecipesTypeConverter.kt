package com.patel.tanmay.recigle.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.patel.tanmay.recigle.models.FoodRecipe

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe) : String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data : String) : FoodRecipe {
        val listType = object  : TypeToken<FoodRecipe>() {}.type
        return  gson.fromJson(data,listType )
    }

    @TypeConverter
    fun resultToString(result : com.patel.tanmay.recigle.models.Result) : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data : String) : com.patel.tanmay.recigle.models.Result {
        val listType = object  : TypeToken<com.patel.tanmay.recigle.models.Result>() {}.type
        return  gson.fromJson(data,listType )
    }

}