package com.example.shortbooks

data class BookItem(
    val id: Int,
    val content: String, // 책 구절
    val title: String,   // 제목
    val author: String,  // 저자
    val link: String,    // 구매 링크
    val review: String,  // 한줄평 (추가됨)
    var isFavorite: Int  // 즐겨찾기 상태
)