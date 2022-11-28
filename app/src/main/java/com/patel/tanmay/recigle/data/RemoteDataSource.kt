package com.patel.tanmay.recigle.data

import com.patel.tanmay.recigle.data.network.FoodRecipesApi
import com.patel.tanmay.recigle.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
   suspend fun getRecipes(queries: Map<String,String>) : Response<FoodRecipe> {
       return foodRecipesApi.getRecipes(queries)
    }
}