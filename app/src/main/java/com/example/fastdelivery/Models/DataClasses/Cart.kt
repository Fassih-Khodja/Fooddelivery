package com.example.fastdelivery.Models.DataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cart(
    val name:String="",
    val price:Int=0,
    var quantity:Int=0,
    val imageUrl: String=""
    //   val image: Nothing? =null
): Parcelable
