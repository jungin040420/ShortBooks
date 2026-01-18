package com.example.shortbooks

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddSentenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sentence)

        // DB 헬퍼 초기화
        val dbHelper = DBHelper(this)

        // 뷰 바인딩: 도서 제목, 문장 내용, 한줄평, 저장 버튼
        val etBookTitle = findViewById<EditText>(R.id.etTitle) // 도서 제목 입력창
        val etContent = findViewById<EditText>(R.id.etContent) // 문장 내용 입력창
        val etComment = findViewById<EditText>(R.id.etComment) // 한줄평 입력창 (추가 필요)
        val btnSave = findViewById<Button>(R.id.btnSave)       // 저장 버튼

        // 데이터 수신: Intent 전달 값 처리
        val bookTitle = intent.getStringExtra("BOOK_TITLE")
        val bookAuthor = intent.getStringExtra("BOOK_AUTHOR")
        if (bookTitle != null) {
            etBookTitle.setText(bookTitle) // 자동 입력
        }

        // 클릭 리스너: 문장 데이터 저장
        btnSave.setOnClickListener {
            val title = etBookTitle.text.toString()
            val content = etContent.text.toString()
            val comment = etComment?.text?.toString() ?: "" // 한줄평 텍스트

            // 유효성 검사: 제목과 문장이 모두 있을 때만 저장
            if (title.isNotEmpty() && content.isNotEmpty()) {

                // 데이터베이스 저장: (문장내용, 제목, 작가, 링크, 한줄평)
                dbHelper.addCard(content, title, bookAuthor ?: "작가 미상", "", comment)

                // 알림 및 화면 종료
                Toast.makeText(this, "문장이 수집되었습니다!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // 경고 알림
                Toast.makeText(this, "도서 제목과 문장 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}