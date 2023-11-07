package com.example.secondhandmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.secondhandmarket.databinding.ActivityDetailScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailScreenBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showText()

        binding.sendMsg.setOnClickListener{
            //메시지 보내는 화면으로 이동
        }
    }
    fun showText() { //판매 글 상세히 보여주는 함수
        database = FirebaseDatabase.getInstance().getReference("Items")
        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //val item = snapshot.getValue(ItemModel::class.java)
                val item = snapshot.children.firstOrNull()?.getValue(ItemModel::class.java)
                binding.title.text = item?.title
                binding.description.text = item?.description
                binding.price.text = item?.price
                binding.soldTF.text = item?.status
                binding.name.text = item?.seller
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailScreenActivity, "error: 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
