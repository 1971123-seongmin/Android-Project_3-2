package com.example.secondhandmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.secondhandmarket.databinding.ActivityDetailScreenBinding
import com.example.secondhandmarket.databinding.ActivityModfiyScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ModifyScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModfiyScreenBinding
    private lateinit var database: DatabaseReference
    private lateinit var itemKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModfiyScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showText()

        binding.modify.setOnClickListener{
            navigateToEditScreen()
        }
    }

    private fun navigateToEditScreen() {
        val itemKey = intent.getStringExtra("itemKey").toString()
        val editIntent = Intent(this, EditScreenActivity::class.java)
        editIntent.putExtra("itemKey", itemKey)
        startActivity(editIntent)
    }

    fun showText() { //판매 글 상세히 보여주는 함수

        val key = intent.getStringExtra("itemKey").toString() //Items 밑에 고유한 키 값 받아옴
        database = FirebaseDatabase.getInstance().getReference("Items").child(key)

        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //val item = snapshot.getValue(ItemModel::class.java)
                val item = snapshot.getValue(ItemModel::class.java)
                binding.title.setText(item?.title)
                binding.description.setText(item?.description)
                binding.price.setText(item?.price)
                binding.soldTF.setText(item?.status)
                binding.name.setText(item?.seller)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ModifyScreenActivity, "error: 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
