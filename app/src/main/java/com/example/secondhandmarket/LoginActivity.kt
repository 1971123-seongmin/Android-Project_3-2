package com.example.secondhandmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.secondhandmarket.databinding.ActivityLoginBinding
import com.example.secondhandmarket.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val pwd = binding.pwd.text.toString()

            auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this) {
                task ->
                    if(task.isSuccessful) {  //로그인 성공한 경우
                       val user = auth.currentUser // 현재 인증된 사용자의 ID
                        //updateUI(user)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
            }

        }
    }


}