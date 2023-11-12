package com.example.secondhandmarket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WritePostActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var uploadButton: Button
    private lateinit var backButton2: Button
    private var cnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.writepost)

        // Firebase Realtime Database 초기화
        dbRef = FirebaseDatabase.getInstance().getReference("Items")

        val mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = mAuth.currentUser

        titleEditText = findViewById(R.id.cTitle)
        descriptionEditText = findViewById(R.id.cDes)
        priceEditText = findViewById(R.id.cPrice)
        backButton2 = findViewById(R.id.backbutton2)
        backButton2.setOnClickListener{
           finish()
        }
        uploadButton = findViewById(R.id.writepostbtn)
        uploadButton.setOnClickListener(View.OnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString()
            val userName = currentUser?.email


            // 글 작성 및 업로드
            uploadPost(title, description, price, userName)
            finish()
        })
    }

    // 글 작성 및 업로드
    private fun uploadPost(title: String?, description: String?, price: String?, seller: String?) {
        val formattedPrice = "$price 원"
        val itemStatus = "판매 중"

        cnt = intent.getIntExtra("cnt", 0).toInt() // Intent로부터 cnt 값을 읽어옴
        val sellerPath = seller?.replace(".", "_")
        val uniqueKey = "$sellerPath${cnt++}" // 각 글마다 고유한 키를 생성

        val itemData = hashMapOf(
            "title" to title,
            "description" to description,
            "price" to formattedPrice,
            "status" to itemStatus,
            "seller" to seller
        )

        val newItemRef = dbRef.child(uniqueKey) //글작성자 + 번호로 고유 키 값 생성
        newItemRef.setValue(itemData)
            .addOnSuccessListener {
                val documentId = newItemRef.key
               Toast.makeText(this,"업로드 성공",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"업로드 실패",Toast.LENGTH_SHORT).show()
            }
    }
}
