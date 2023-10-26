package com.example.secondhandmarket

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.secondhandmarket.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val HomeFragment by lazy { HomeFragment() }
    private val ChatFragment by lazy { ChatFragment() }
    private val MyPageFragment by lazy { MyPageFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val a = 333
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
