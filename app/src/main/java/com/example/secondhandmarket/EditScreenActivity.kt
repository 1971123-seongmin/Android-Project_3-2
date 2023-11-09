package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.secondhandmarket.databinding.ActivityEditScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditScreenBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showText()

        binding.updateButton.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        val key = intent.getStringExtra("itemKey").toString()
        val updatedTitle = binding.title.text.toString()
        val updatedDescription = binding.description.text.toString()
        val updatedPrice = binding.price.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Items").child(key)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(ItemModel::class.java)

                // 기존 데이터 유지 및 업데이트
                val updatedData = hashMapOf(
                    "title" to updatedTitle,
                    "description" to updatedDescription,
                    "price" to updatedPrice,
                    "status" to item?.status,  // 기존 값 유지
                    "seller" to item?.seller   // 기존 값 유지
                )

                // 데이터베이스 업데이트
                database.setValue(updatedData)
                    .addOnSuccessListener {
                        val resultIntent = Intent()
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // 실패 시 동작
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
                Toast.makeText(this@EditScreenActivity, "error: 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showText() {
        val key = intent.getStringExtra("itemKey").toString()
        database = FirebaseDatabase.getInstance().getReference("Items").child(key)

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(ItemModel::class.java)
                binding.title.setText(item?.title)
                binding.description.setText(item?.description)
                binding.price.setText(item?.price)
                binding.soldTF.setText(item?.status)
                binding.name.setText(item?.seller)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditScreenActivity, "error: 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

}