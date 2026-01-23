package com.example.shortbooks

// 문장 정보 데이터 모델
data class SentenceItem(
    val id: Int,           // 데이터 식별자 (ID)
    val content: String,   // 수집된 문장 내용
    val bookTitle: String, // 도서 제목
    val author: String,    // 도서 저자
    val image: String?,    // 도서 표지 이미지 경로 (Null 허용)
    val buyLink: String?,  // 도서 구매/상세 링크 (Null 허용)
    var isStarred: Int     // 즐겨찾기 등록 여부 (0: 해제, 1: 등록)
)