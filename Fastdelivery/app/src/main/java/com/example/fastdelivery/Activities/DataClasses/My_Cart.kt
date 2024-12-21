package com.example.fastdelivery.Activities.DataClasses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastdelivery.Adapters.MyCartAdapter
import com.example.fastdelivery.Adapters.OnCartItemChangeQuantity
import com.example.fastdelivery.Decorations.Decoration_Items
import com.example.fastdelivery.Models.DataClasses.Cart
import com.example.fastdelivery.Models.DataClasses.Orders
import com.example.fastdelivery.Models.Repositories.OrdersFetch_repository
import com.example.fastdelivery.Models.Repositories.Orderschange_repository
import com.example.fastdelivery.ViewModels.MyOrders_view_model
import com.example.fastdelivery.databinding.ActivityMyCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class My_Cart : AppCompatActivity(), OnCartItemChangeQuantity {
    private lateinit var binding:ActivityMyCartBinding
    private lateinit var cartlist:ArrayList<Cart>
    private lateinit var cartadapter:MyCartAdapter
    private lateinit var model: MyOrders_view_model
    val db = FirebaseFirestore.getInstance()
    val auth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val ordersrepo= OrdersFetch_repository(db,auth.currentUser!!.uid)
        val orderschangerepo= Orderschange_repository(db,auth.currentUser!!.uid)
        model= ViewModelProvider(this, MyOrders_view_model.Factory(ordersrepo,orderschangerepo)).get(
            MyOrders_view_model::class.java)

        Log.d("activity","has been created")
        cartlist= intent.getParcelableArrayListExtra("CartList")!!
        Log.d("test",cartlist.toString())
        cartadapter=MyCartAdapter(cartlist,this)
        binding.mycartrecyclerview.adapter=cartadapter
        val spaceItemDecoration = Decoration_Items(50,binding.mycartrecyclerview)
        binding.mycartrecyclerview.addItemDecoration(spaceItemDecoration)
        binding.mycartrecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    /*    binding.test.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra("CartList2", cartlist)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }*/
        binding.placeorderbtn.setOnClickListener {
            model.AddOrder(Orders(cartlist,"",0))
        }
    }


    override fun finish() {
        val resultIntent = Intent()
        Log.d("the setresult",cartlist.toString())
        resultIntent.putParcelableArrayListExtra("CartList2", cartlist)
        setResult(Activity.RESULT_OK, resultIntent)
        super.finish()
    }

    override fun oncartitemclchangequantity(position: Int) {
        Log.d("this ",cartlist[position].quantity.toString())
    }

}