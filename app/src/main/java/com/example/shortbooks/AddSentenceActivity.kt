package com.example.shortbooks

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate // 명사형 주석: 날짜 라이브러리 추가

class AddSentenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sentence)

        val dbHelper = DBHelper(this)

        val etBookTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val etComment = findViewById<EditText>(R.id.etComment)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val bookAuthor = intent.getStringExtra("BOOK_AUTHOR")
        val bookImage = intent.getStringExtra("BOOK_IMAGE")
        val bookLink = intent.getStringExtra("BOOK_LINK")

        if (bookTitle != null) {
            etBookTitle.setText(bookTitle)
        }

        btnSave.setOnClickListener {
            val title = etBookTitle.text.toString()
            val content = etContent.text.toString()
            val comment = etComment?.text?.toString() ?: ""

            if (title.isNotEmpty() && content.isNotEmpty()) {

                // 1. 현재 날짜 생성 (yyyy-MM-dd 형식)
                // 명사형 주석: 달력 표시용 날짜 데이터 생성
                val todayDate = LocalDate.now().toString()

                // 2. 데이터베이스 저장 (수정된 addCard 호출)
                // 명사형 주석: 마지막 인자로 생성된 날짜 전달
                dbHelper.addCard(
                    content,
                    title,
                    bookAuthor ?: "작가 미상",
                    bookLink ?: "",
                    comment,
                    bookImage ?: "",
                    todayDate // record_date 컬럼에 저장됨
                )

                Toast.makeText(this, "문장 수집 완료! 달력에 기록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "도서 제목과 문장 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}