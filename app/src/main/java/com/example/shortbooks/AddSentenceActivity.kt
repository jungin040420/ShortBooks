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

        // DB 관리 클래스(DBHelper) 초기화
        val dbHelper = DBHelper(this)

        // 레이아웃의 뷰(View) 연결
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // [완료] 버튼 클릭 시 동작
        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            // 입력값이 비어있지 않은지 확인
            if (title.isNotEmpty() && content.isNotEmpty()) {
                // DBHelper를 통해 SQLite 데이터베이스에 저장
                // (content, title, author, link, review 순서)
                dbHelper.addCard(content, title, "작가 미상","","")

                Toast.makeText(this, "문장이 저장되었습니다!", Toast.LENGTH_SHORT).show()

                // 저장 후 현재 화면을 닫고 메인으로 이동
                finish()
            } else {
                Toast.makeText(this, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}