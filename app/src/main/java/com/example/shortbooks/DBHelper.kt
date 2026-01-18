package com.example.shortbooks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ShortBook.db", null, 3) {

    // 데이터베이스 테이블 초기 생성
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "author TEXT, " +
                "content TEXT, " +
                "image TEXT, " +
                "link TEXT, " +
                "review TEXT, " +
                "startdate TEXT, " +
                "enddate TEXT, " +
                "is_reading INTEGER DEFAULT 0, " +
                "is_favorite INTEGER DEFAULT 0)") // 모든 문자열을 하나로 합친 후 괄호를 닫아야 합니다.
    }

    // DB 버전 업그레이드 시 처리 (기존 데이터 삭제 후 재생성)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Books")
        onCreate(db)
    }

    // 카드 데이터 신규 저장
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
    // 도서 데이터 신규 저장
    fun addBook(title: String, author: String, startdate: String, enddate: String,
                isReading: Int, review: String, image: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("author", author)
            put("startdate", startdate)
            put("enddate", enddate)
            put("is_reading", isReading)
            put("review", review)
            put("image", image)
        }
        db.insert("Books", null, values)
        db.close()
    }

    // 즐겨찾기 상태 업데이트
    fun updateFavorite(id: Int, isFav: Int) {
        writableDatabase.execSQL("UPDATE Books SET is_favorite = $isFav WHERE id = $id")
    }

    // 저장된 전체 데이터 조회 (내 서재 화면 연결용)
    fun getAllData(): List<BookItem> {
        val list = mutableListOf<BookItem>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM Books ORDER BY RANDOM()", null)

        while (cursor.moveToNext()) {
            list.add(BookItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                author = cursor.getString(cursor.getColumnIndexOrThrow("author")),
                content = cursor.getString(cursor.getColumnIndexOrThrow("content")) ?: "",
                image = cursor.getString(cursor.getColumnIndexOrThrow("image")) ?: "",
                link = cursor.getString(cursor.getColumnIndexOrThrow("link")) ?: "",
                review = cursor.getString(cursor.getColumnIndexOrThrow("review")),
                startDate = cursor.getString(cursor.getColumnIndexOrThrow("startdate")) ?: "",
                endDate = cursor.getString(cursor.getColumnIndexOrThrow("enddate")) ?: "",
                isReading = cursor.getInt(cursor.getColumnIndexOrThrow("is_reading")),
                isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite"))
            ))
        }
        cursor.close()
        return list
    }
}