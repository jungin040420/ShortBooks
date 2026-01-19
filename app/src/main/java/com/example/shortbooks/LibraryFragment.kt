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

    private lateinit var dbHelper: DBHelper
    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 헬퍼 초기화
        dbHelper = DBHelper(requireContext())

        // 리사이클러뷰 참조 및 격자 레이아웃(3열) 설정
        recyclerView = view.findViewById(R.id.rv_library)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // 메뉴 아이콘 클릭 시 리스트 메뉴 표시
        val ivMenu = view.findViewById<ImageView>(R.id.iv_menu)
        // LibraryFragment.kt 내 ivMenu 클릭 리스너
        ivMenu.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menuInflater.inflate(R.menu.menu_library, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_my_sentences -> {
                        // 내 문장 액티비티로 이동
                        val intent = Intent(requireContext(), MySentencesActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        // 도서 추가 버튼 설정
        val btnAdd = view.findViewById<View>(R.id.btn_add_book)
        btnAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddBookActivity::class.java)
            startActivity(intent)
        }

        // 초기 데이터 로드
        refreshBooks()
    }

    /**
     * 화면이 다시 보일 때마다 도서 목록 새로고침
     */
    override fun onResume() {
        super.onResume()
        refreshBooks()
    }

    /**
     * 도서 전용 데이터를 로드하여 어댑터 갱신
     */
    private fun refreshBooks() {
        // [수정] getAllData 대신 도서 전용 함수인 getAllBooks 사용
        // 이로써 문장만 추가된 데이터가 서재에 중복으로 뜨는 현상 방지
        val bookList = dbHelper.getAllBooks()

        // 어댑터 생성 및 연결
        bookAdapter = BookAdapter(bookList)
        recyclerView.adapter = bookAdapter

        // 데이터 변경 알림
        bookAdapter.notifyDataSetChanged()
    }
}