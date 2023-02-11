package com.patel.tanmay.recigle

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patel.tanmay.recigle.models.FoodRecipe
import com.patel.tanmay.recigle.util.Constants.Companion.RECIPE_TABLE

@Entity(tableName = RECIPE_TABLE)
class RecipeEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}