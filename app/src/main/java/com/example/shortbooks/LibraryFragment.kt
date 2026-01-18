package com.example.shortbooks

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 내 서재 메인 화면 프래그먼트
 */
class LibraryFragment : Fragment(R.layout.fragment_library) {

    // 클래스 전체에서 사용할 수 있도록 변수를 밖으로 뺍니다.
    private lateinit var dbHelper: DBHelper
    private lateinit var bookAdapter: BookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 헬퍼 초기화
        dbHelper = DBHelper(requireContext())

        // 초기 데이터 로드
        val bookList = dbHelper.getAllData()

        // 리사이클러뷰 참조 및 설정
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_library)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // 어댑터 연결 (클래스 변수 bookAdapter에 할당)
        bookAdapter = BookAdapter(bookList)
        recyclerView.adapter = bookAdapter

        // 도서 추가 버튼 설정
        val btnAdd = view.findViewById<View>(R.id.btn_add_book)
        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddBookActivity::class.java)
            startActivity(intent)
        }
    }

    // 중요: onResume은 onViewCreated 바깥에(클래스 바로 아래) 있어야 합니다.
    override fun onResume() {
        super.onResume()

        // 1. DB에서 최신 리스트 가져오기
        val newList = dbHelper.getAllData()

        // 2. 어댑터에 데이터 갱신 알리기
        bookAdapter.setData(newList)
        bookAdapter.notifyDataSetChanged()
    }
}