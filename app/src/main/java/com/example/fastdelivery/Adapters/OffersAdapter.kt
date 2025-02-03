package com.example.fastdelivery.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.R

class OffersAdapter(val l:ArrayList<String>):RecyclerView.Adapter<Offersviewholder>() { // this is the class of adapter
    //The Adapter creates ViewHolder objects as needed and also sets the data for those view


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Offersviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.offers_item,parent,false)
        return Offersviewholder(inflater)
    }//calls this method whenever it needs to create a new ViewHolde

    override fun getItemCount(): Int {
        return  l.size
    }

    override fun onBindViewHolder(holder: Offersviewholder, position: Int) {
        holder.image.load(l[position])
    }
}
class Offersviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.imageoffer)
} //The ViewHolder is a wrapper around a View that contains the layout for an individual item in the list ..so the view holder is for a single item layout
/*remarc : the class of view holder is not a class of the adapter , no they are different but they work together
 RecyclerView.ViewHolder
 RecyclerView.Adapter */