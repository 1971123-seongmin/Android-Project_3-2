package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandmarket.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var selectedStatus: String? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var storageRef: DatabaseReference
    private lateinit var adapter: ItemAdapter
    private lateinit var itemList: MutableList<ItemModel>
    private var writeButton: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // FAB (버튼) 클릭 이벤트 처리
        writeButton = binding.write
        writeButton?.setOnClickListener {
            // FAB 버튼 클릭 시 WritePostActivity로 전환
            val intent = Intent(this@HomeFragment.requireActivity(), WritePostActivity::class.java)
            startActivity(intent)
        }

        // 팝업 메뉴 코드 추가
        val menuFilter = binding.menuFilter
        menuFilter?.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), menuFilter)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sell_progress -> {
                        // "판매중" 메뉴 아이템 클릭 시 처리
                        selectedStatus = "판매 중"
                        updateItemList()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.sell_completed -> {
                        // "판매완료" 메뉴 아이템 클릭 시 처리
                        selectedStatus = "판매완료"
                        updateItemList()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.menu_show_all -> {
                        // "모두" 메뉴 아이템 클릭 시 처리
                        selectedStatus = null
                        updateItemList()
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }

            popupMenu.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getItemData()
    }

    private fun updateItemList() {
        val filteredItems = when (selectedStatus) {
            "판매 중" -> {
                itemList.filter { it.status == "판매 중"}
            }
            "판매완료" -> {
                itemList.filter { it.status == "판매완료" }
            }
            else -> {
                itemList
            }
        }

        adapter.updateList(filteredItems)
    }

    private fun init() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        itemList = mutableListOf()
        adapter = ItemAdapter(itemList) // 어댑터 초기화
        binding.recyclerView.adapter = adapter // RecyclerView에 어댑터 설정
    }

    fun getItemData() {
        val itemRecyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        itemRecyclerView?.visibility = View.GONE

        storageRef = FirebaseDatabase.getInstance().reference.child("Items")

        // "status" 필드가 "판매 중"인 아이템만 가져오도록 쿼리 설정
        storageRef.orderByChild("status").equalTo("판매 중").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                if (snapshot.exists()) {
                    for (itemSnap in snapshot.children) {
                        val itemData = itemSnap.getValue(ItemModel::class.java)
                        itemList.add(itemData!!)
                    }

                    val mAdapter = ItemAdapter(itemList)
                    itemRecyclerView?.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ItemAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireContext(), MainActivity::class.java)

                            intent.putExtra("itemImg", itemList[position].imgUri)
                            intent.putExtra("itemTitle", itemList[position].title)
                            intent.putExtra("itemStatus", itemList[position].status)
                            intent.putExtra("itemPrice", itemList[position].price)

                            startActivity(intent)
                        }
                    })

                    binding.recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}