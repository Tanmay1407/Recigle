package com.patel.tanmay.recigle.data

import com.patel.tanmay.recigle.RecipeDao
import com.patel.tanmay.recigle.RecipeEntity
import com.patel.tanmay.recigle.data.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource  @Inject constructor(
    private val recipeDao: RecipeDao
){
     fun readRecipes() : Flow<List<RecipeEntity>> {
         return recipeDao.readRecipes()
     }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipeDao.readFavoriteRecipes()
    }

   suspend fun insertRecipes(recipeEntity: RecipeEntity){
        recipeDao.insertRecipes(recipeEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipeDao.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipeDao.deleteFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipeDao.deleteAllFavoriteRecipes()
    }


}