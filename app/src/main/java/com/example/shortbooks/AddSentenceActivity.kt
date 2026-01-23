package com.example.shortbooks

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class AddSentenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sentence)

        // DB 헬퍼 객체 초기화
        val dbHelper = DBHelper(this)

        // UI 컴포넌트 연결
        val etBookTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val etComment = findViewById<EditText>(R.id.etComment)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Intent 전달 데이터 수신 (도서 상세 정보)
        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val bookAuthor = intent.getStringExtra("BOOK_AUTHOR")
        val bookImage = intent.getStringExtra("BOOK_IMAGE")
        val bookLink = intent.getStringExtra("BOOK_LINK")

        // 도서 제목 자동 입력 처리
        if (bookTitle != null) {
            etBookTitle.setText(bookTitle)
        }

        // 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener {
            // 입력 데이터 가공
            val title = etBookTitle.text.toString()
            val content = etContent.text.toString()
            val comment = etComment?.text?.toString() ?: ""

            // 필수 입력값 검증 (제목, 내용)
            if (title.isNotEmpty() && content.isNotEmpty()) {

                // 현재 날짜 생성 (yyyy-MM-dd)
                val todayDate = LocalDate.now().toString()

                // DB 데이터 삽입 (문장 카드 정보 저장)
                dbHelper.addCard(
                    content,
                    title,
                    bookAuthor ?: "작가 미상",
                    bookLink ?: "",
                    comment,
                    bookImage ?: "",
                    todayDate
                )

                // 성공 메시지 출력 및 액티비티 종료
                Toast.makeText(this, "문장 수집 완료! 달력에 기록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // 입력 누락 안내
                Toast.makeText(this, "도서 제목과 문장 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}