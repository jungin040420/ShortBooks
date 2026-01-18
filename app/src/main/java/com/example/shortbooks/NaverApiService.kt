package com.example.shortbooks

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// 네이버 검색 데이터 구조 정의
data class NaverBookResponse(val items: List<NaverBookItem>)
data class NaverBookItem(
    val title: String,
    val image: String,
    val author: String
)

interface NaverApiService {
    @GET("v1/search/book.json")
    fun searchBook(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String
    ): Call<NaverBookResponse>
}