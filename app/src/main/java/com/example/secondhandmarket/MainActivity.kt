package com.example.secondhandmarket

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.secondhandmarket.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val HomeFragment by lazy { HomeFragment() }
    private val ChatFragment by lazy { ChatFragment() }
    private val MyPageFragment by lazy { MyPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val email = user?.email

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show()

        //아래 3줄은 데이터베이스 확인용 코드임!!
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

        initNavigationBar()
    }

    private fun initNavigationBar() {
        val nav = findViewById<BottomNavigationView>(R.id.nav)
        nav.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(HomeFragment)
                    }
                    R.id.chat -> {
                        changeFragment(ChatFragment)
                    }
                    R.id.myPage -> {
                        changeFragment(MyPageFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun changeFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Frame, fragment)
            .commit()
    }
}
