package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhandmarket.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var itemList: ArrayList<ItemModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        itemList = arrayListOf<ItemModel>()

        val recyclerView = binding?.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        val mAdapter = ItemAdapter(itemList)
        recyclerView?.adapter = mAdapter
        /*

        val cardview =binding?.cardView
        cardview?.setOnClickListener() {

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        */
        getItemData()

        return binding?.root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        binding = null
    }

    fun getItemData() {
        val recyclerView = binding?.recyclerView
        recyclerView?.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("Items")

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                if (snapshot.exists()){
                    for(itemSnap in snapshot.children){
                        val itemData = itemSnap.getValue(ItemModel::class.java)
                        itemList.add(itemData!!)
                    }
                }
                recyclerView?.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}