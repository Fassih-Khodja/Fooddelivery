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

class FoodAdapter(var l:List<Food>?, private  val listner:FoodonItemClickListner): RecyclerView.Adapter<Foodviewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Foodviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.foods_item,parent,false)
        return Foodviewholder(inflater)
    }

    override fun getItemCount(): Int {
        return l?.size ?: 0
    }

    override fun onBindViewHolder(holder: Foodviewholder, position: Int) {

        holder.image.load(l?.get(position)?.imageUrl){
        }
        holder.name.text= l?.get(position)?.name ?: ""
        holder.price.text= l?.get(position)?.price.toString()
        holder.image.transitionName = "image_$position"
      //  holder.stars.text= l?.get(position)?.stars.toString()
        holder.itemView.setOnClickListener {
            l?.get(position)?.let { it1 -> listner.foodonitemclick(holder.image,position, it1) }
        }

    }

    fun updatefoods(newfood: List<Food>) {
        l = newfood
        notifyDataSetChanged()
    }
}
class Foodviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.foodimage)
    val name=itemView.findViewById<TextView>(R.id.foodname)
    val price=itemView.findViewById<TextView>(R.id.foodprice)

    //val stars=itemView.findViewById<TextView>(R.id.star_text)

}
interface FoodonItemClickListner{
    fun foodonitemclick(imageView: ImageView,position:Int,dataclass_item:Food)
}
