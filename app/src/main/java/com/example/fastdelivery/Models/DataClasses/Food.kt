package com.example.fastdelivery.Models.DataClasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "foods")
data class Food(
    @PrimaryKey
    val name:String="",
    val price:Int=0,
    val categorie:String="",
    val bestseller:Boolean=false,
    val imageUrl: String=""
): Parcelable
