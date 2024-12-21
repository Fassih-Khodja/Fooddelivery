package com.example.fastdelivery.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.Models.DataClasses.Categorie_food
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Home_view_model

class CategoriesAdapter(var l: List<Categorie_food>?, private val model:Home_view_model,private  val listner:onItemClickListner): RecyclerView.Adapter<CategoriesAdapter.Categoriesviewholder>() { // this is the class of adapter
    //The Adapter creates ViewHolder objects as needed and also sets the data for those view

    private var selectedPosition: Int = 0 // the selected position is to change the color of items depending on which one is clicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Categoriesviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.categories_item,parent,false)
        return Categoriesviewholder(inflater)
    }//calls this method whenever it needs to create a new ViewHolde

    override fun getItemCount(): Int {
        return l?.size ?: 0
    }

    override fun onBindViewHolder(holder: Categoriesviewholder, @SuppressLint("RecyclerView") position: Int) {

          holder.name.text= l?.get(position)?.name ?: ""
        holder.image.load(l?.get(position)?.imageUrl)


        if (selectedPosition==position) {
            holder.itemContainer.setCardBackgroundColor(Color.parseColor("#FFD27C"))

        } else {
            holder.itemContainer.setCardBackgroundColor(Color.WHITE)
            }
        holder.itemView.setOnClickListener {

            val previousPosition = selectedPosition
            selectedPosition=position
            notifyItemChanged(position)
             notifyItemChanged(previousPosition)
            listner.onitemclick(position)

            //l?.get(position)?.name?.let { it1 -> model.foodcategory(it1) } // this is not a good practice i should do the callback for the adapter
            //maybe by using interface or listener .. i have to think about it goodly

        }
    }
    fun updateCategories(newCategories: List<Categorie_food>) {
        l = newCategories
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
    inner class Categoriesviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
        val image=itemView.findViewById<ImageView>(R.id.categorieimage)
        val name=itemView.findViewById<TextView>(R.id.categoriename)
        val itemContainer= itemView.findViewById<CardView>(R.id.cardcontainer)

    }
}
interface onItemClickListner{
    fun onitemclick(position:Int)
}
