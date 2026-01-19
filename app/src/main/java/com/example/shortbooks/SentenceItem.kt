package com.example.shortbooks

data class SentenceItem(
    val id: Int,
    val content: String,
    val bookTitle: String,
    val author: String,       //
    val image: String?,   //
    val buyLink: String?,     //
    var isStarred: Int        //
)