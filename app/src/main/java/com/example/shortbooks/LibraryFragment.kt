package com.example.shortbooks

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 내 서재 메인 화면 프래그먼트
 */
class LibraryFragment : Fragment(R.layout.fragment_library) {

    // UI 객체 및 DB 헬퍼 선언
    private lateinit var dbHelper: DBHelper
    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 헬퍼 초기화
        dbHelper = DBHelper(requireContext())

        // 리사이클러뷰 참조 및 레이아웃(3열 그리드) 설정
        recyclerView = view.findViewById(R.id.rv_library)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // 메뉴 아이콘 참조
        val ivMenu = view.findViewById<ImageView>(R.id.iv_menu)

        // 팝업 메뉴 설정 및 클릭 이벤트
        ivMenu.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.menu_library, popup.menu)

            // 메뉴 항목별 액션 처리 (내 문장 화면 이동)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_my_sentences -> {
                        val intent = Intent(requireContext(), MySentencesActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        // 도서 추가 버튼 참조 및 클릭 이벤트 (도서 추가 화면 이동)
        val btnAdd = view.findViewById<View>(R.id.btn_add_book)
        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddBookActivity::class.java)
            startActivity(intent)
        }

        // 초기 데이터 로드 및 UI 반영
        refreshBooks()
    }

    // 화면 복귀 시 도서 목록 최신화
    override fun onResume() {
        super.onResume()
        refreshBooks()
    }

    /**
     * 도서 데이터 로드 및 어댑터 갱신
     */
    private fun refreshBooks() {
        // DB 내 도서 목록 추출 (중복 제거 데이터)
        val bookList = dbHelper.getAllBooks()

        // 어댑터 초기화 및 데이터 연결
        bookAdapter = BookAdapter(bookList)
        recyclerView.adapter = bookAdapter

        // UI 갱신 알림
        bookAdapter.notifyDataSetChanged()
    }
}