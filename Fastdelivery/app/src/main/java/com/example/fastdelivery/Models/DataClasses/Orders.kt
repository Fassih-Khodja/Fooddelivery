package com.example.fastdelivery.Models.DataClasses

data class Orders(
    val order:List<Cart> = emptyList(),
    val date:String="12/10/2014",
    val totalprice:Int=0
)
