package com.patel.tanmay.recigle.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.patel.tanmay.recigle.RecipeEntity
import com.patel.tanmay.recigle.data.Repository
import com.patel.tanmay.recigle.data.database.entities.FavoritesEntity
import com.patel.tanmay.recigle.models.FoodRecipe
import com.patel.tanmay.recigle.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application) : AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readRecipes : LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipe : LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipeEntity: RecipeEntity) =
        viewModelScope.launch (Dispatchers.IO ) {
            repository.local.insertRecipes(recipeEntity)
        }

     fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch (Dispatchers.IO ) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

     fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch (Dispatchers.IO ) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

     fun deleteAllFavoriteRecipes() =
        viewModelScope.launch (Dispatchers.IO ) {
            repository.local.deleteAllFavoriteRecipes()
        }




    /** RETROFIT */
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries : Map<String,String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery : Map<String,String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }



    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value  = NetworkResult.Loading();

            if(hasInternetConnection()){
                try {
                    val response = repository.remote.getRecipes(queries)
                    recipesResponse.value = handleFoodRecipesResponse(response)

                    val foodRecipe = recipesResponse.value!!.data
                    if(foodRecipe != null){
                        offlineCacheRecipes(foodRecipe)
                    }
                }catch (e : Exception){
                    recipesResponse.value = NetworkResult.Error("Recipes not found.")
                }
            }else {
                recipesResponse.value = NetworkResult.Error("No Internet Connection.")
            }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value  = NetworkResult.Loading()

        if(hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)
            }catch (e : Exception){
                searchRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        }else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipeEntity(foodRecipe)
        insertRecipes(recipeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
            when {
                response.message().toString().contains("timeout") -> {
                    return NetworkResult.Error("Timeout")
                }

                response.code() == 402 -> {
                    return NetworkResult.Error("API Key Limited.")
                }

                response.body()!!.results.isNullOrEmpty() -> {
                    return NetworkResult.Error("Recipes not found.")
                }

                response.isSuccessful -> {
                    val foodRecipes = response.body()
                    return NetworkResult.Success(foodRecipes!!)
                }

                else -> {
                    return NetworkResult.Error(response.message())
                }
            }
    }

    private fun hasInternetConnection() : Boolean {
            val connectivityManager = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false

            }
        }
}