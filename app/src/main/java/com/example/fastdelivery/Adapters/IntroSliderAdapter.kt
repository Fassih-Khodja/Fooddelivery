package com.example.fastdelivery.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fastdelivery.Models.DataClasses.SliderItems
import com.example.fastdelivery.R

class IntroSliderAdapter(val l:ArrayList<SliderItems>):RecyclerView.Adapter<IntroSliderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.introslider,parent,false)
        return IntroSliderViewHolder(inflater)
    }

    override fun getItemCount(): Int {
       return  l.size
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
holder.title.text=l[position].title
        holder.description.text=l[position].description
        holder.image.setImageResource(l[position].image)
    }
}
class IntroSliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
val title=itemView.findViewById<TextView>(R.id.titleimage)
    val description=itemView.findViewById<TextView>(R.id.discriptionimage)
    val image=itemView.findViewById<ImageView>(R.id.imageslider)
}