package com.example.secondhandmarket

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
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
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var selectedStatus: String? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var storageRef: DatabaseReference
    private lateinit var adapter: ItemAdapter
    private lateinit var itemList: MutableList<ItemModel>
    private var writeButton: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //로그아웃 버튼
        val logoutButton = binding.menuLogout
        logoutButton.setOnClickListener{
            showCustomDialog()
        }

        // FAB (버튼) 클릭 이벤트 처리
        writeButton = binding?.write
        writeButton?.setOnClickListener {
            // FAB 버튼 클릭 시 WritePostActivity로 전환
            val intent = Intent(this@HomeFragment.requireActivity(), WritePostActivity::class.java)
            startActivity(intent)
        }
        
        //채팅 보기 페이지
        val menuChat = binding.menuChat
        menuChat.setOnClickListener {
            val intent = Intent(requireContext(), showChatActivity::class.java)
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

                        selectedStatus = "판매 완료"
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
    override fun onResume() { //항상 화면이 업데이트 되도록 함
        super.onResume()
        getItemData()
    }

    //로그아웃 다이얼로그를 띄우는 함수
    private fun showCustomDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.logout_dialog, null)

        val auth = FirebaseAuth.getInstance()

        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true) //다이얼로그 취소 가능함
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val logoutMsg : TextView = myDialog.findViewById(R.id.logoutMsg)
        val btnYes : Button = myDialog.findViewById(R.id.btnYes)
        val btnNo : Button = myDialog.findViewById(R.id.btnNo)

        logoutMsg.text = "로그아웃 하시겠습니까?"

        btnYes.setOnClickListener{
            auth.signOut()
            //query.removeEventListener(valueEventListener)

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            myDialog.dismiss()
        }

        btnNo.setOnClickListener{
            myDialog.dismiss()
        }

        myDialog.show()
    }

    private fun updateItemList() {
        val filteredItems = when (selectedStatus) {
            "판매 중" -> {
                itemList.filter { it.status == "판매 중" }
            }
            "판매 완료" -> {
                itemList.filter { it.status == "판매 완료" }
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
        //query.addValueEventListener(object : ValueEventListener

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

                }
                adapter.updateList(itemList)
                binding.recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("오류", "error:불러오기 실패")
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
