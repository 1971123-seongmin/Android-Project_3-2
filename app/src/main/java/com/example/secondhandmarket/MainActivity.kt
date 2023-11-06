package com.example.secondhandmarket

import android.app.appsearch.AppSearchSchema.BooleanPropertyConfig
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

import com.example.secondhandmarket.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val HomeFragment by lazy { HomeFragment() }
    private val ChatFragment by lazy { ChatFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(R.layout.toolbar)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val email = user?.email

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show()

        //아래 3줄은 데이터베이스 확인용 코드임!! A
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, Worlda!")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_chat -> {
                Toast.makeText(applicationContext, "채팅 실행", Toast.LENGTH_LONG).show()
                return true
            }

            R.id.menu_filter -> {
                Toast.makeText(applicationContext, "검색조건 실행", Toast.LENGTH_LONG).show()
                return true
            }

            R.id.board_write -> {
                Toast.makeText(applicationContext, "글쓰기 실행", Toast.LENGTH_LONG).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
        fun changeFragment(fragment: Fragment) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.Frame, fragment)
                .commit()
        }
    }
}
