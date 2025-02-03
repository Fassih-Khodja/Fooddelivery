package com.example.fastdelivery.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.R

class FavoritAdapter (var l:List<Food>?): RecyclerView.Adapter<Favoritviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favoritviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.favorit_item_layout,parent,false)
        return Favoritviewholder(inflater)
    }

    override fun onBindViewHolder(holder: Favoritviewholder, position: Int) {

        holder.image.load(l?.get(position)?.imageUrl)
        holder.name.text= l?.get(position)?.name ?: ""
        holder.price.text= l?.get(position)?.price.toString()
       // holder.image.transitionName = "image_$position"
    }

    override fun getItemCount(): Int {
        return l?.size ?: 0
    }

}
class Favoritviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.foodimage)
    val name=itemView.findViewById<TextView>(R.id.foodname)
    val price=itemView.findViewById<TextView>(R.id.foodprice)

    //val stars=itemView.findViewById<TextView>(R.id.star_text)

}
