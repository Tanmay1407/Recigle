package com.patel.tanmay.recigle.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.patel.tanmay.recigle.RecipeDao
import com.patel.tanmay.recigle.RecipeEntity
import com.patel.tanmay.recigle.data.database.entities.FavoritesEntity

@Database(
    entities = [RecipeEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao() : RecipeDao
}