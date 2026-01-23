package com.example.shortbooks

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 네이버 도서 검색 API 응답 데이터 구조 (JSON 매핑용 모델)
data class NaverBookResponse(val items: List<NaverBookItem>)

// 개별 도서 상세 정보 모델
data class NaverBookItem(
    val title: String,  // 도서 제목
    val image: String,  // 도서 표지 이미지 URL
    val author: String  // 도서 저자
)

// Retrofit API 인터페이스 정의
interface NaverApiService {
    // 도서 검색 엔드포인트 설정 (GET 방식)
    @GET("v1/search/book.json")
    fun searchBook(
        // API 인증 정보 (클라이언트 ID)
        @Header("X-Naver-Client-Id") clientId: String,
        // API 인증 정보 (클라이언트 시크릿)
        @Header("X-Naver-Client-Secret") clientSecret: String,
        // 검색 키워드 쿼리
        @Query("query") query: String
    ): Call<NaverBookResponse> // 검색 결과 응답 객체 반환
}