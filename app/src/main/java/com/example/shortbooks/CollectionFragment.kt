package com.example.shortbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

/**
 * 문장 수집(Shorts) 콘텐츠 표시 프래그먼트
 */
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private lateinit var viewPager: ViewPager2
    private lateinit var dbHelper: DBHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 참조 및 헬퍼 초기화
        viewPager = view.findViewById(R.id.viewPager)
        dbHelper = DBHelper(requireContext())

        // 세로 스와이프 설정
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        // 데이터 로드 및 어댑터 연결
        loadSentences()
    }

    // [추가] 다른 화면에서 문장을 추가하고 돌아왔을 때 리스트 갱신
    override fun onResume() {
        super.onResume()
        loadSentences()
    }

    /**
     * 문장 내용이 있는 데이터만 로드하여 화면 업데이트
     */
    private fun loadSentences() {
        // [수정] getAllData 대신 문장 전용 데이터(getSentenceData) 호출
        // 이로써 문장을 추가하기 전까지는 화면에 아무것도 뜨지 않음
        val sentenceList = dbHelper.getSentenceData()

        // 어댑터 설정
        viewPager.adapter = ShortsAdapter(sentenceList, dbHelper)
    }
}