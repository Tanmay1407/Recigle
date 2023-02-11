package com.patel.tanmay.recigle.util

class Constants {
    // companion object acts as static member which can be called without object
    companion object {
        const val API_KEY = "60a95886259f43e69506805ab60b6e57"
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

        const val RECIPE_RESULT_KEY = "recipeBundle"

        // API QUERY KEYS :
        const val  QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFO= "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS= "fillIngredients"

        // Room database :
        const val DATABASE_NAME = "recipe_database"
        const val RECIPE_TABLE = "recipe_table"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"

        //Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "vegetarian"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"




    }
}