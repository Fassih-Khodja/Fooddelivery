package com.example.fastdelivery.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.Models.DataClasses.Cart
import com.example.fastdelivery.R

class MyCartAdapter(var l:MutableList<Cart>,private  val listner:OnCartItemChangeQuantity): RecyclerView.Adapter<CartViewHolde>() {

    var isEditMode = false // the first as he get created gonne be false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolde {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.cart_item,parent,false)
        return CartViewHolde(inflater)
    }

    override fun getItemCount(): Int {
        return l.size
    }

    override fun onBindViewHolder(holder: CartViewHolde, position: Int) {
        holder.name.text= l.get(position).name
        holder.price.text= (l.get(position).price*l.get(position).quantity ).toString()
        holder.quantity.text=l.get(position).quantity.toString()
        holder.image.load(l[position].imageUrl)
        if (isEditMode) {
            holder.deletebtn.apply {
                visibility = View.VISIBLE
                holder.deletebtn.translationY = 100f // Start below
                holder.deletebtn.alpha = 0f
                holder.deletebtn.animate()
                    .translationY(0f) // Move up
                    .alpha(1f) // Fade in
                    .setDuration(300)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        } else {
            holder.deletebtn.animate()
                .translationY(100f) // Move down
                .alpha(0f) // Fade out
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction { holder.deletebtn.visibility = View.GONE }
                .start()
        }


        holder.deletebtn.apply {
            setOnClickListener {
                removeItem(position)
            }
        }

        holder.plusbtn.setOnClickListener {
            l[position].quantity++
         //   listner.oncartitemclchangequantity(position)
            //remarc important  : Both cartlist and l (the list in your adapter) are likely pointing to the same underlying list of items in memory.
            // so when i change the cartlist (the oadress of the object) and use l.get..etc it gonna find already changed
            holder.quantity.text=(l.get(position).quantity).toString()
            holder.price.text= (l.get(position).price*l.get(position).quantity).toString()

        }
        holder.minusbtn.setOnClickListener {
            if (l[position].quantity>1) {
                l[position].quantity--
                //listner.oncartitemclchangequantity(position) ; i don't need to this cuz it's the same adress
                holder.quantity.text = (l.get(position).quantity).toString()
                holder.price.text = (l.get(position).price * l.get(position).quantity).toString()
            }
        }

    }

    private fun removeItem(position: Int) {
        l.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, l.size)

    }

    fun tooglemode(){
        isEditMode=!isEditMode
       // notifyDataSetChanged()
        notifyItemRangeChanged(0, l.size)
    }


}
class CartViewHolde(itemView: View): RecyclerView.ViewHolder(itemView){ // this is the class of view holder
    val image=itemView.findViewById<ImageView>(R.id.foodimage)
    val name=itemView.findViewById<TextView>(R.id.foodname)
    val price=itemView.findViewById<TextView>(R.id.foodpricetotal)
    val plusbtn=itemView.findViewById<ImageButton>(R.id.plus_btn)
    val minusbtn=itemView.findViewById<ImageButton>(R.id.minusbtn)
    val quantity=itemView.findViewById<TextView>(R.id.cartquantity)
    val deletebtn=itemView.findViewById<ImageButton>(R.id.x_delete_btn)
    //val stars=itemView.findViewById<TextView>(R.id.star_text)

}
interface OnCartItemChangeQuantity{
     fun oncartitemclchangequantity(position: Int)
}