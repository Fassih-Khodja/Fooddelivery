package com.example.fastdelivery.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fastdelivery.Models.DataClasses.Food

@Database(entities = [Food::class], version = 1)
abstract class AppDataBase:RoomDatabase() {
    abstract fun foodDao(): FoodDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "foods"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}