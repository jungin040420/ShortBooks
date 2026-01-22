package com.example.shortbooks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ShortBook.db", null, 8) {

    // 테이블 생성
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "author TEXT, " +
                "content TEXT, " +
                "image TEXT, " +
                "link TEXT, " +
                "comment TEXT, " +
                "review TEXT, " +
                "startdate TEXT, " +
                "enddate TEXT, " +
                "is_reading INTEGER DEFAULT 0, " +
                "is_favorite INTEGER DEFAULT 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Books")
        onCreate(db)
    }

    // 문장 수집 데이터 조회
    fun getSentenceData(): List<BookItem> {
        val list = mutableListOf<BookItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Books WHERE content IS NOT NULL AND content != '' ORDER BY id DESC", null)

        while (cursor.moveToNext()) {
            list.add(cursorToBookItem(cursor))
        }
        cursor.close()
        return list
    }

    // 모든 도서 데이터 조회
    fun getAllBooks(): List<BookItem> {
        val list = mutableListOf<BookItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Books WHERE image IS NOT NULL AND image != '' GROUP BY title ORDER BY id DESC", null)

        while (cursor.moveToNext()) {
            list.add(cursorToBookItem(cursor))
        }
        cursor.close()
        return list
    }

    // 커서 데이터 객체 변환
    private fun cursorToBookItem(cursor: android.database.Cursor): BookItem {
        return BookItem(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            title = cursor.getString(cursor.getColumnIndexOrThrow("title")) ?: "",
            author = cursor.getString(cursor.getColumnIndexOrThrow("author")) ?: "",
            content = cursor.getString(cursor.getColumnIndexOrThrow("content")) ?: "",
            image = cursor.getString(cursor.getColumnIndexOrThrow("image")) ?: "",
            link = cursor.getString(cursor.getColumnIndexOrThrow("link")) ?: "",
            comment = cursor.getString(cursor.getColumnIndexOrThrow("comment")) ?: "",
            review = cursor.getString(cursor.getColumnIndexOrThrow("review")) ?: "",
            startDate = cursor.getString(cursor.getColumnIndexOrThrow("startdate")) ?: "",
            endDate = cursor.getString(cursor.getColumnIndexOrThrow("enddate")) ?: "",
            isReading = cursor.getInt(cursor.getColumnIndexOrThrow("is_reading")),
            isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite"))
        )
    }

    // 카드 데이터 저장
    fun addCard(content: String, title: String, author: String, link: String, comment: String, image: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("content", content)
            put("title", title)
            put("author", author)
            put("link", link)
            put("comment", comment)
            put("image", image)
            put("is_favorite", 0)
        }
        db.insert("Books", null, values)
        db.close()
    }

    // 도서 데이터 저장
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

    // 즐겨찾기 상태 반전 업데이트
    fun updateStarStatus(id: Int): Int {
        val db = this.writableDatabase
        var newStatus = 0
        val cursor = db.rawQuery("SELECT is_favorite FROM Books WHERE id = ?", arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            val currentStatus = cursor.getInt(0)
            newStatus = if (currentStatus == 1) 0 else 1
            db.execSQL("UPDATE Books SET is_favorite = $newStatus WHERE id = $id")
        }
        cursor.close()
        db.close()
        return newStatus
    }

    // 즐겨찾기 특정 상태 업데이트
    fun updateStarStatus(id: Int, status: Int) {
        val db = this.writableDatabase
        db.execSQL("UPDATE Books SET is_favorite = $status WHERE id = $id")
        db.close()
    }

    // 전체 문장 리스트 조회
    fun getAllSentences(): List<SentenceItem> {
        val list = mutableListOf<SentenceItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Books WHERE content IS NOT NULL AND content != ''", null)

        while (cursor.moveToNext()) {
            list.add(SentenceItem(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("content")),
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getString(cursor.getColumnIndexOrThrow("author")),
                cursor.getString(cursor.getColumnIndexOrThrow("image")),
                cursor.getString(cursor.getColumnIndexOrThrow("link")),
                cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite"))
            ))
        }
        cursor.close()
        return list
    }

    // 즐겨찾기 문장 목록 조회
    fun getStarredSentences(): MutableList<SentenceItem> {
        val list = mutableListOf<SentenceItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Books WHERE is_favorite = 1 ORDER BY id DESC", null)

        while (cursor.moveToNext()) {
            list.add(SentenceItem(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("content")) ?: "",
                cursor.getString(cursor.getColumnIndexOrThrow("title")) ?: "",
                cursor.getString(cursor.getColumnIndexOrThrow("author")) ?: "",
                cursor.getString(cursor.getColumnIndexOrThrow("image")) ?: "",
                cursor.getString(cursor.getColumnIndexOrThrow("link")) ?: "",
                cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite"))
            ))
        }
        cursor.close()
        return list
    }
}