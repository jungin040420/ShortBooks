package com.example.shortbooks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 네비게이션 아이템 선택 리스너 설정
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_text -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CollectionFragment()).commit()
                    true
                }
                R.id.menu_library -> {
                    // '내 서재' 클릭 시 LibraryFragment로 이동
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LibraryFragment()).commit()
                    true
                }
                R.id.menu_mypage -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MyPageFragment()).commit()
                    true
                }
                else -> false
            }
        }

        // [추가된 로직] MySentencesActivity에서 '내 서재'로 돌아올 때 처리
        val target = intent.getStringExtra("target")
        if (target == "library") {
            // 인텐트로 "library" 신호가 오면 바로 내 서재 탭을 선택합니다.
            bottomNav.selectedItemId = R.id.menu_library
        } else if (savedInstanceState == null) {
            // 앱이 처음 실행될 때만 기본 탭(메인)을 선택합니다.
            bottomNav.selectedItemId = R.id.menu_text
        }
    }

    // [참고] 내 문장에서 돌아올 때 MainActivity가 이미 켜져 있다면 이 함수가 호출됩니다.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val target = intent.getStringExtra("target")
        if (target == "library") {
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.menu_library
        }
    }
}
