package com.example.fastdelivery.Models.DataClasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Food(
    val name:String="",
    val price:Int=0,
    val categorie:String="",
    val bestseller:Boolean=false,
   val imageUrl: String=""
): Parcelable
