package com.example.fastdelivery.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastdelivery.Adapters.MyOrdersAdapter
import com.example.fastdelivery.Decorations.Decoration_Items
import com.example.fastdelivery.Models.Repositories.OrdersFetch_repository
import com.example.fastdelivery.Models.Repositories.Orderschange_repository
import com.example.fastdelivery.ViewModels.MyOrders_view_model
import com.example.fastdelivery.databinding.FragmentMyOrdersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class My_Orders : Fragment() {
    private lateinit var recyclerviewmyorders: RecyclerView
    private lateinit var myordersadapter: MyOrdersAdapter
    private lateinit var binding: FragmentMyOrdersBinding
    private lateinit var model: MyOrders_view_model
    val db = FirebaseFirestore.getInstance()
    val auth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ordersrepo=OrdersFetch_repository(db,auth.currentUser!!.uid)
        val orderschangerepo=Orderschange_repository(db,auth.currentUser!!.uid)
        model= ViewModelProvider(this, MyOrders_view_model.Factory(ordersrepo,orderschangerepo)).get(
            MyOrders_view_model::class.java)


        recyclerviewmyorders=binding.myordersrecyclerview

        myordersadapter= MyOrdersAdapter(model.listorders.value?: emptyList())

        model.listorders.observe(viewLifecycleOwner) { newlist ->
            if (newlist != null) {
                Log.d("null","the categories are not null")

                myordersadapter.updateOrders(newlist)
                // Handle the categories data, e.g., update a RecyclerView adapter
            } else             Log.d("null","the categories are null")
        }




        recyclerviewmyorders.adapter=myordersadapter
        val spaceItemDecoration = Decoration_Items(60,recyclerviewmyorders)
        recyclerviewmyorders.addItemDecoration(spaceItemDecoration)
        recyclerviewmyorders.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }


}