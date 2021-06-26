package com.example.spoc_v2.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spoc_v2.models.Result
import com.example.spoc_v2.util.Constants.Companion.FAVORITES_RECIPES_TABLE


@Entity(tableName = FAVORITES_RECIPES_TABLE )
class FavoritesEntity (
    @PrimaryKey(autoGenerate= true)
    var id: Int,
    var result: Result)