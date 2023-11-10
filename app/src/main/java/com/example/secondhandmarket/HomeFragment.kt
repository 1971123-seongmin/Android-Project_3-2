package com.example.secondhandmarket

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {
    private var selectedStatus: String? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var storageRef: DatabaseReference
    private lateinit var adapter: ItemAdapter
    private lateinit var itemList: MutableList<ItemModel>
    private var writeButton: FloatingActionButton? = null
    private var cnt = 0; //파이어베이스 db에 저장될 고유 키 번호

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // FAB (버튼) 클릭 이벤트 처리
        writeButton = binding?.write
        writeButton?.setOnClickListener {
            cnt ++
            // FAB 버튼 클릭 시 WritePostActivity로 전환
            val intent = Intent(this@HomeFragment.requireActivity(), WritePostActivity::class.java)
            intent.putExtra("cnt", cnt) // cnt 값을 Intent에 추가
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
                        selectedStatus = "판매중"
                        updateItemList()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.sell_completed -> {

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
            "판매중" -> {
                itemList.filter { it.status == "판매중" }
            }
            "판매완료" -> {
                itemList.filter { it.status == "판매완료" }
            }
            else -> {
                itemList
            }
        }
        Log.d(selectedStatus, "")
        adapter.updateList(filteredItems)
    }

    private fun init(){
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        itemList = mutableListOf()
        adapter = ItemAdapter(itemList) // 어댑터 초기화
        binding.recyclerView.adapter = adapter
    }

    fun getItemData() {
        val itemRecyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        itemRecyclerView?.visibility = View.GONE

        storageRef = FirebaseDatabase.getInstance().reference.child("Items")

        val query = if (selectedStatus != null) {
            // 선택된 상태에 따라 Firebase 쿼리 설정
            storageRef.orderByChild("status").equalTo(selectedStatus)
        } else {
            storageRef // 모든 항목 가져오기
        }

        val keys = mutableListOf<String>() //DB에서 Items 밑에 고유 키값을 가져오기위한 list

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                if (snapshot.exists()) {

                    for (itemSnap in snapshot.children) {
                        val key = itemSnap.key
                        keys.add(key!!) // 고유 키값을 keys 리스트에 추가

                        val itemData = itemSnap.getValue(ItemModel::class.java)
                        itemList.add(itemData!!)
                    }

                    // 여기서 itemList를 업데이트하고 어댑터에 새 목록을 설정
                    adapter.updateList(itemList)
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })


        val userEmail = FirebaseAuth.getInstance().currentUser?.email //현재 사용자의 email

        //리사이클러뷰의 아이템 클릭 이벤트
        adapter.setOnItemClickListener(object : ItemAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val ClickedItemSeller = itemList[position].seller.toString()

                if(userEmail == ClickedItemSeller) { //현재 사용자의 email과 글 작성자 email 동일하면 수정 화면으로 이동
                    val intent = Intent(requireContext(), ModifyScreenActivity::class.java)
                    val itemKey = keys[position] // 클릭한 아이템의 고유 키값 가져오기
                    intent.putExtra("itemKey", itemKey) // 클릭한 아이템의 고유 키값을 Intent에 넣어서 전달
                    startActivity(intent)

                } else { //다르면 판매 글 보기 화면으로 이동
                    val intent2 = Intent(requireContext(), DetailScreenActivity::class.java)
                    val itemKey = keys[position] // 클릭한 아이템의 고유 키값 가져오기
                    intent2.putExtra("itemKey", itemKey) // 클릭한 아이템의 고유 키값을 Intent에 넣어서 전달
                    startActivity(intent2)
                }
            }
        })
    }
}
