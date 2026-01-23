package com.example.shortbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

/**
 * 문장 수집(Shorts) 콘텐츠 표시 프래그먼트
 */
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    // UI 및 DB 객체 선언
    private lateinit var viewPager: ViewPager2
    private lateinit var dbHelper: DBHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 참조 및 DB 헬퍼 초기화
        viewPager = view.findViewById(R.id.viewPager)
        dbHelper = DBHelper(requireContext())

        // 뷰페이저 세로 스와이프 모드 설정
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        // 데이터 로드 및 UI 반영
        loadSentences()
    }

    // 화면 복귀 시 데이터 최신화 (갱신 로직)
    override fun onResume() {
        super.onResume()
        loadSentences()
    }

    /**
     * 문장 데이터 로드 및 어댑터 연결
     */
    private fun loadSentences() {
        // DB에서 저장된 문장 리스트 추출
        val sentenceList = dbHelper.getSentenceData()

        // 쇼츠 어댑터 생성 및 연결
        viewPager.adapter = ShortsAdapter(sentenceList, dbHelper)
    }
}