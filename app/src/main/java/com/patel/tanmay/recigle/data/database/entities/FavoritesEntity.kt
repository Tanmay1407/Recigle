package com.patel.tanmay.recigle.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.patel.tanmay.recigle.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val result : com.patel.tanmay.recigle.models.Result
        )