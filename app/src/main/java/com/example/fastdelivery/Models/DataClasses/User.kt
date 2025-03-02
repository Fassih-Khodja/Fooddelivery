package com.example.fastdelivery.Models.DataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class User(
    val full_name:String="",
    val email:String="",
    val phone_number:String="",
    val bio:String=""
): Parcelable
