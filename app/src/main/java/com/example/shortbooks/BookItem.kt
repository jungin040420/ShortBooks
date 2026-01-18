package com.example.shortbooks

/**
 * 도서 및 문장 정보를 담는 데이터 클래스
 */
data class BookItem(
    val id: Int,
    val title: String,      // 제목
    val author: String,     // 저자
    val content: String,    // 책 구절 (카드용)
    val image: String,      // 도서 표지 이미지 URL 또는 경로
    val link: String,       // 상세 링크
    val review: String,     // 한줄평
    val startDate: String,  // 읽기 시작한 날
    val endDate: String,    // 다 읽은 날
    val isReading: Int,     // 읽는 중 여부 (0: 다읽음, 1: 읽는중)
    var isFavorite: Int     // 즐겨찾기 상태 (0: 안함, 1: 즐겨찾기)
)