package com.example.shortbooks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // XML의 ID와 동일한지 확인하세요: @id/bottom_navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_text -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menu_text, CollectionFragment()).commit()
                    true
                }
                R.id.menu_library -> {
                    // '내 서재' 클릭 시 LibraryFragment로 이동
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menu_library, LibraryFragment()).commit()
                    true
                }
                R.id.menu_mypage -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.menu_mypage, MyPageFragment()).commit()
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.menu_text
        }
    }
}
