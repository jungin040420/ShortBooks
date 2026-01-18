package com.example.shortbooks

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 도서 등록 화면 액티비티 (네이버 API 연동 버전)
 */
class AddBookActivity : AppCompatActivity() {

    private var selectedImageUrl = "" // 이미지 URL 저장 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        // 화면 뷰 참조
        val etSearch = findViewById<EditText>(R.id.et_search) // 검색창
        val etTitle = findViewById<EditText>(R.id.et_title)
        val etAuthor = findViewById<EditText>(R.id.et_author)
        val etStartDate = findViewById<EditText>(R.id.et_start_date)
        val etEndDate = findViewById<EditText>(R.id.et_end_date)
        val swReading = findViewById<SwitchCompat>(R.id.sw_reading)
        val etReview = findViewById<EditText>(R.id.et_review)
        val btnComplete = findViewById<TextView>(R.id.btn_complete)
        val ivBookCover = findViewById<ImageView>(R.id.iv_book_cover)
        val ivPlusIcon = findViewById<ImageView>(R.id.iv_plus_icon)

        // 1. 네이버 검색창 이벤트 설정 (키보드 엔터키 클릭 시)
        etSearch.setOnEditorActionListener { v, _, _ ->
            val query = v.text.toString()
            if (query.isNotEmpty()) {
                searchNaverBook(query)
            }
            true
        }

        // 2. 완료 버튼 클릭 리스너 (DB 저장 로직)
        btnComplete.setOnClickListener {
            val title = etTitle.text.toString()
            val author = etAuthor.text.toString()
            val startDate = etStartDate.text.toString()
            val endDate = etEndDate.text.toString()
            val isReading = if (swReading.isChecked) 1 else 0
            val review = etReview.text.toString()

            if (title.isNotEmpty()) {
                val dbHelper = DBHelper(this)
                // 도서 등록 함수 호출 (작성하신 DBHelper의 함수 형식에 맞춤)
                dbHelper.addBook(title, author, startDate, endDate, isReading, review, selectedImageUrl)

                Toast.makeText(this, "등록 완료", Toast.LENGTH_SHORT).show()
                finish() // 화면 종료
            } else {
                Toast.makeText(this, "도서 제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 네이버 도서 검색 API 호출 함수
     */
    private fun searchNaverBook(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NaverApiService::class.java)

        // 네이버 개발자 센터 Client ID와 Secret 입력
        val clientId = "UZfuovuPs8lDqKfNlLTe"
        val clientSecret = "zUPaELFeFz"

        apiService.searchBook(clientId, clientSecret, query).enqueue(object : Callback<NaverBookResponse> {
            override fun onResponse(call: Call<NaverBookResponse>, response: Response<NaverBookResponse>) {
                if (response.isSuccessful) {
                    // 첫 번째 검색 결과만 가져옴
                    val book = response.body()?.items?.firstOrNull()
                    book?.let {
                        // 제목, 저자 필드 자동 채우기
                        findViewById<EditText>(R.id.et_title).setText(it.title)
                        findViewById<EditText>(R.id.et_author).setText(it.author)

                        // 이미지 표시
                        selectedImageUrl = it.image
                        findViewById<ImageView>(R.id.iv_plus_icon).visibility = View.GONE
                        Glide.with(this@AddBookActivity)
                            .load(it.image)
                            .into(findViewById<ImageView>(R.id.iv_book_cover))
                    } ?: Toast.makeText(this@AddBookActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NaverBookResponse>, t: Throwable) {
                Toast.makeText(this@AddBookActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }
}