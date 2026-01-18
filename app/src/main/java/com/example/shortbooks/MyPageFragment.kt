package com.example.shortbooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MyPageFragment : Fragment() { // 마이페이지 화면 프래그먼트
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 미리 만들어둔 fragment_mypage.xml 레이아웃과 연결
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }
}