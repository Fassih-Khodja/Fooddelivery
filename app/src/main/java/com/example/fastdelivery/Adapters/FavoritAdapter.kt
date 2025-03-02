package com.example.fastdelivery.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.R

class FavoritAdapter (var l:List<Food>?, private  val listner:HeartFavoritClickListner): RecyclerView.Adapter<Favoritviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Favoritviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.favorit_item_layout,parent,false)
        return Favoritviewholder(inflater)
    }

    override fun onBindViewHolder(holder: Favoritviewholder, position: Int) {

        holder.image.load(l?.get(position)?.imageUrl)
        holder.name.text= l?.get(position)?.name ?: ""
        holder.price.text= l?.get(position)?.price.toString()
        holder.image.transitionName = "image_f$position"

        holder.heartfavorit.setOnClickListener {
            l?.get(position)?.let { it1 -> listner.heartclick(it1) }
        }
        holder.itemView.setOnClickListener {
            l?.get(position)?.let { it1 -> listner.item_click_listener(holder.image,position, it1) }
        }
       // holder.image.transitionName = "image_$position"
    }

    override fun getItemCount(): Int {
        return l?.size ?: 0
    }
    fun updatefavoritfoodlist(newlist:List<Food>){
        l=newlist
        notifyDataSetChanged()
    }

}
class Favoritviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.foodimage)
    val name=itemView.findViewById<TextView>(R.id.foodname)
    val price=itemView.findViewById<TextView>(R.id.foodprice)
    val heartfavorit=itemView.findViewById<ImageButton>(R.id.favorite_heart)
    //val stars=itemView.findViewById<TextView>(R.id.star_text)

}
interface HeartFavoritClickListner{
    fun heartclick(item_to_remove:Food)
    fun item_click_listener(imageView: ImageView,position:Int,dataclass_item:Food)
}
