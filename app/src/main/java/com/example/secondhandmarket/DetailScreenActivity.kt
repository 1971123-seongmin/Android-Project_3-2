package com.example.secondhandmarket

import android.content.Intent
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
    private lateinit var user : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showText()

        binding.sendMsg.setOnClickListener{
            //메시지 보내는 화면으로 이동
            val intent = Intent(this, chatActivity::class.java)
            intent.putExtra("userEmail", user)
            startActivity(intent)
        }
    }
    fun showText() { //판매 글 상세히 보여주는 함수

        val key = intent.getStringExtra("itemKey").toString() //Items 밑에 고유한 키 값 받아옴
        database = FirebaseDatabase.getInstance().getReference("Items").child(key)

        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                //val item = snapshot.getValue(ItemModel::class.java)
                val item = snapshot.getValue(ItemModel::class.java)
                binding.title.text = item?.title
                binding.description.text = item?.description
                binding.price.text = item?.price
                binding.soldTF.text = item?.status
                binding.name.text = item?.seller

                user = item?.seller?.toString() ?: "DefaultUser"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailScreenActivity, "error: 불러오기 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
