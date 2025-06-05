package com.example.fastdelivery.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fastdelivery.Models.DataClasses.Food

@Dao
interface FoodDAO {
    @Query("SELECT * FROM foods")
    fun getAllfood(): List<Food>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun ainsertorupdatefood(food:Food)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertallthelist(foodList: List<Food>)
    @Query("DELETE FROM foods")
    fun deleteallfoodlist()
}