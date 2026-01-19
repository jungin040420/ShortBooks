package com.example.shortbooks

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// 내 문장 화면 액티비티
class MySentencesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var rvToday: RecyclerView
    private lateinit var rvStarred: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sentences) // 레이아웃 설정

        // DB 및 뷰 초기화
        dbHelper = DBHelper(this)
        rvToday = findViewById(R.id.rv_today_sentences)
        rvStarred = findViewById(R.id.rv_starred_sentences)

        // 1. 오늘의 문장들: 가로 스크롤 리스트 설정
        rvToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val myAllSentences = dbHelper.getAllSentences() // 전체 문장 데이터 로드
        rvToday.adapter = TodayCardAdapter(myAllSentences) // 어댑터 연결

        // 2. 기억하고 싶은 문장들: 세로 스크롤 리스트 설정
        rvStarred.layoutManager = LinearLayoutManager(this)
        val starredList = dbHelper.getStarredSentences() // 즐겨찾기 데이터 로드
        rvStarred.adapter = StarredListAdapter(starredList) // 어댑터 연결

        // 상단 네비게이션 메뉴 설정
        setupNavigationMenu()
    }

    // 메뉴 설정: 팝업 메뉴 및 화면 이동 처리
    private fun setupNavigationMenu() {
        val ivMenu = findViewById<ImageView>(R.id.iv_menu) // 메뉴 아이콘 참조
        ivMenu.setOnClickListener { v ->
            val popup = PopupMenu(this, v)
            popup.menuInflater.inflate(R.menu.menu_my_sentences, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_library -> {
                        // 메인 액티비티 이동 및 내 책장 탭 신호 전달
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("target", "library") // 탭 전환 데이터 전송
                        startActivity(intent)
                        finish() // 현재 화면 종료
                        true
                    }
                    else -> false
                }
            }
            popup.show() // 팝업 메뉴 표시
        }
    }
}