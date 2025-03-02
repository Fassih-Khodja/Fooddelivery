package com.example.fastdelivery.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fastdelivery.Models.DataClasses.Orders
import com.example.fastdelivery.R

class MyOrdersAdapter(var l:List<Orders>):RecyclerView.Adapter<OrdersViewHolde>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolde {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.orders_recyclerview_item,parent,false)
        return OrdersViewHolde(inflater)
    }

    override fun getItemCount(): Int = l.size


    override fun onBindViewHolder(holder: OrdersViewHolde, position: Int) {
      /*  holder.date.text=l[position].date
        holder.totalprice.text=l[position].totalprice.toString()*/
        holder.linearlayout.removeAllViews()
        for (order in l[position].order){
            val order_cart_item = LayoutInflater.from(holder.linearlayout.context)
                .inflate(R.layout.order_cart_item, holder.linearlayout, false)
            order_cart_item.findViewById<TextView>(R.id.foodname).text=order.name
            order_cart_item.findViewById<TextView>(R.id.foodpricetotal).text=order.price.toString()
            order_cart_item.findViewById<ImageView>(R.id.foodimage).load(order.imageUrl){
            }
            holder.linearlayout.addView(order_cart_item)
        }
    }
    fun updateOrders(newCategories: List<Orders>) {
        l = newCategories
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}

class OrdersViewHolde(itemView: View): RecyclerView.ViewHolder(itemView){
val linearlayout=itemView.findViewById<LinearLayout>(R.id.orderslinearlayout)
  /*  val date=itemView.findViewById<TextView>(R.id.dateorder)
    val totalprice=itemView.findViewById<TextView>(R.id.totalpriceorder)*/
}