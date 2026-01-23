package com.example.shortbooks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 하단 네비게이션 바 참조
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 네비게이션 아이템 선택 리스너 (프래그먼트 교체 로직)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_text -> {
                    // 문장 수집(Collection) 화면 전환
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CollectionFragment()).commit()
                    true
                }
                R.id.menu_library -> {
                    // 내 서재(Library) 화면 전환
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LibraryFragment()).commit()
                    true
                }
                R.id.menu_mypage -> {
                    // 마이페이지(MyPage) 화면 전환
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MyPageFragment()).commit()
                    true
                }
                else -> false
            }
        }

        // 인텐트 데이터 수신 (이동 목표 확인)
        val target = intent.getStringExtra("target")

        if (target == "library") {
            // 특정 화면 호출 시 '내 서재' 탭 강제 선택
            bottomNav.selectedItemId = R.id.menu_library
        } else if (savedInstanceState == null) {
            // 앱 최초 실행 시 기본 탭(문장 수집) 설정
            bottomNav.selectedItemId = R.id.menu_text
        }
    }

    // 액티비티 재호출 시 인텐트 처리 (싱글탑 대응)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 새 인텐트 설정
        setIntent(intent)

        val target = intent.getStringExtra("target")
        if (target == "library") {
            // '내 서재' 탭으로 화면 갱신
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.menu_library
        }
    }
}
