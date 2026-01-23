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

    // DB 및 UI 객체 선언
    private lateinit var dbHelper: DBHelper
    private lateinit var rvToday: RecyclerView
    private lateinit var rvStarred: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sentences)

        // 객체 및 뷰 초기화
        dbHelper = DBHelper(this)
        rvToday = findViewById(R.id.rv_today_sentences)
        rvStarred = findViewById(R.id.rv_starred_sentences)

        // 오늘의 문장 리스트 설정 (가로 스크롤 방식)
        rvToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val myAllSentences = dbHelper.getAllSentences()
        rvToday.adapter = TodayCardAdapter(myAllSentences)

        // 즐겨찾기 문장 리스트 설정 (세로 스크롤 방식)
        rvStarred.layoutManager = LinearLayoutManager(this)
        val starredList = dbHelper.getStarredSentences()
        rvStarred.adapter = StarredListAdapter(starredList)

        // 상단 네비게이션 메뉴 초기화
        setupNavigationMenu()
    }

    // 네비게이션 팝업 메뉴 설정
    private fun setupNavigationMenu() {
        // 메뉴 아이콘 참조
        val ivMenu = findViewById<ImageView>(R.id.iv_menu)

        // 메뉴 클릭 이벤트 처리
        ivMenu.setOnClickListener { v ->
            val popup = PopupMenu(this, v)
            popup.menuInflater.inflate(R.menu.menu_my_sentences, popup.menu)

            // 메뉴 항목별 액션 정의
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_library -> {
                        // 메인 화면 이동 및 특정 탭(내 서재) 지정 데이터 전달
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("target", "library")
                        startActivity(intent)

                        // 현재 화면 종료 (스택 관리)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            // 팝업 메뉴 출력
            popup.show()
        }
    }
}