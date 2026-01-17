package com.example.shortbooks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ShortBook.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // 테이블 생성
        db.execSQL("CREATE TABLE Books (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, title TEXT, author TEXT, link TEXT, review TEXT, is_favorite INTEGER DEFAULT 0)")

        // 샘플 데이터 삽입 (review 인자 포함)
        //insertSample(db, "해동 육룡이 날으샤...", "용비어천가", "정인지 등", "https://www.aladin.co.kr", "과거와 현재를 잇는 위대한 문장입니다.")
        //insertSample(db, "뿌리 깊은 나무는...", "용비어천가", "정인지 등", "https://naver.com", "심지가 굳은 사람이 되고 싶게 만드는 구절이네요.")
    }

    //샘플 출력용 함수, 후에 카드 등록 -> 출력 (사용하지 않을 함수)
    /*private fun insertSample(db: SQLiteDatabase, content: String, title: String, author: String, link: String, review: String) {
        val values = ContentValues().apply {
            put("content", content)
            put("title", title)
            put("author", author)
            put("link", link)
            put("review", review)
        }
        db.insert("Books", null, values)
    }*/

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    //카드 등록 출력 함수 (현재 카드 등록 페이지 생성 X)
    fun addCard(content: String, title: String, author: String, link: String, review: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("content", content)
            put("title", title)
            put("author", author)
            put("link", link)
            put("review", review)
        }
        db.insert("Books", null, values)
    }

    fun updateFavorite(id: Int, isFav: Int) {
        writableDatabase.execSQL("UPDATE Books SET is_favorite = $isFav WHERE id = $id")
    }

    fun getAllData(): List<com.example.shortbooks.BookItem> {
        val list = mutableListOf<com.example.shortbooks.BookItem>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM Books ORDER BY RANDOM()", null)
        while (cursor.moveToNext()) {
            list.add(com.example.shortbooks.BookItem(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), // review
                cursor.getInt(6) // isFavorite
            ))
        }
        cursor.close()
        return list
    }
}
