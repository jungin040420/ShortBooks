package com.example.shortbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

/**
 * 도서 요약(Shorts) 콘텐츠 표시를 위한 프래그먼트
 */
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    // 뷰 생성 완료 시점의 초기화 로직
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 슬라이드 컴포넌트(ViewPager2) 참조
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // 로컬 DB 접근을 위한 헬퍼 객체 생성
        val dbHelper = DBHelper(requireContext())

        // 어댑터 생성 및 데이터 바인딩 (전체 도서 데이터 로드)
        viewPager.adapter = ShortsAdapter(dbHelper.getAllData(), dbHelper)

        // 페이지 전환 방향 설정 (세로 스와이프 방식)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
}