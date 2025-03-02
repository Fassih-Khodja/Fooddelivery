package com.example.fastdelivery.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.Models.DataClasses.Food
import com.example.fastdelivery.R

class SearchAdapter (var l:List<Food>?,private val listner:searchitemclicklistner): RecyclerView.Adapter<Searchviewholder>(), Filterable {
    private var filteredList = l?.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Searchviewholder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout,parent,false)
        return Searchviewholder(inflater)
    }

    override fun onBindViewHolder(holder: Searchviewholder, position: Int) {

        holder.image.load(l?.get(position)?.imageUrl){

        }
        holder.name.text= filteredList?.get(position)?.name ?: ""
         holder.image.transitionName = "image_s$position"
        holder.itemView.setOnClickListener {

            l?.get(position)?.let { it1 -> listner.searchonitemclick(position, it1,holder.image) }

        }
    }

    override fun getItemCount(): Int {
        return filteredList?.size ?: 0
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchText = constraint.toString().lowercase()
                filteredList = if (searchText.isEmpty()) {
                    l?.toMutableList()}
                else {
                    l?.filter { it.name.lowercase().contains(searchText) }?.toMutableList()
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Food>
                notifyDataSetChanged()
            }

        }
    }

}
class Searchviewholder(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.foodimage)
    val name=itemView.findViewById<TextView>(R.id.foodname)

    //val stars=itemView.findViewById<TextView>(R.id.star_text)

}
interface searchitemclicklistner{
    fun searchonitemclick(position:Int,dataclass_item:Food,imageView: ImageView)
}