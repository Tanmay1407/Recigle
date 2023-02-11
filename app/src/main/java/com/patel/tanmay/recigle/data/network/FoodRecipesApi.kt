package com.patel.tanmay.recigle.data.network

import com.patel.tanmay.recigle.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    // suspend keyword -> used to perform task in background (Kotlin-coroutines)

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries : Map<String,String>
    ) : Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery : Map<String,String>
    ) : Response<FoodRecipe>

}