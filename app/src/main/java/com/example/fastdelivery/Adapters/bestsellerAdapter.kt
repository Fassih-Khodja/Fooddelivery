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

class bestsellerAdapter(var l:List<Food>?, private val listner:bestselleronitemclicklistner): RecyclerView.Adapter<bestsellerviewholder>() { // this is the class of adapter
    //The Adapter creates ViewHolder objects as needed and also sets the data for those view


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bestsellerviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.bestseller_item,parent,false)
        return bestsellerviewholder(inflater)
    }//calls this method whenever it needs to create a new ViewHolde

    override fun getItemCount(): Int {
        return l?.size ?: 0
    }

    override fun onBindViewHolder(holder: bestsellerviewholder, position: Int) {
        holder.name.text = l?.get(position)?.name ?: ""
        holder.image.load(l?.get(position)?.imageUrl){
        }
       holder.image.transitionName = "image_b${position}" // il faux changer le faix de +100 , il faux de trouver une autre method
        holder.itemView.setOnClickListener {

                l?.get(position)?.let { it1 -> listner.bestselleronitemclick(position, it1,holder.image) }

        }
    }

    fun updatebestsellers(bestsellers: List<Food>) {
            l = bestsellers
            notifyDataSetChanged()
        }

}
class bestsellerviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.bestsellerimage)
    val name=itemView.findViewById<TextView>(R.id.bestsellername)
   /* val price=itemView.findViewById<TextView>(R.id.bestsellerprice)*/
}
interface bestselleronitemclicklistner{
    fun bestselleronitemclick(position:Int,dataclass_item:Food,imageView: ImageView)
}