package com.example.shortbooks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "ShortBook.db", null, 6) { // 버전 업그레이드

    // 테이블 생성: 도서 및 카드 데이터 통합 관리
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " + // 도서 제목
                "author TEXT, " + // 저자 이름
                "content TEXT, " + // 문장 내용 (카드용)
                "image TEXT, " + // 표지 이미지 경로
                "link TEXT, " + // 관련 링크
                "comment TEXT, " + // 카드용 한줄평 (etComment)
                "review TEXT, " + // 도서용 한줄독후감 (etReview)
                "startdate TEXT, " + // 독서 시작일
                "enddate TEXT, " + // 독서 종료일
                "is_reading INTEGER DEFAULT 0, " + // 독서 중 상태
                "is_favorite INTEGER DEFAULT 0)") // 즐겨찾기 상태
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Books")
        onCreate(db)
    }

    // [중요] 문장이 추가된 데이터만 조회 (문장 수집 화면용)
    // content 필드가 비어있지 않은 데이터만 필터링합니다.
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

    // [중요] 모든 도서 데이터 조회 (내 서재 화면용)
    // 이미지가 있는 도서 등록 데이터를 위주로 가져옵니다.
    fun getAllBooks(): List<BookItem> {
        val list = mutableListOf<BookItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Books WHERE image IS NOT NULL AND image != '' ORDER BY id DESC", null)

        while (cursor.moveToNext()) {
            list.add(cursorToBookItem(cursor))
        }
        cursor.close()
        return list
    }

    // 공통 커서 변환 로직
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

    // 카드 데이터 저장 (etComment 사용)
    fun addCard(content: String, title: String, author: String, link: String, comment: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("content", content)
            put("title", title)
            put("author", author)
            put("link", link)
            put("comment", comment)
        }
        db.insert("Books", null, values)
        db.close()
    }

    // 도서 데이터 저장 (etReview 사용)
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

    fun updateFavorite(id: Int, isFav: Int) {
        val db = this.writableDatabase
        db.execSQL("UPDATE Books SET is_favorite = $isFav WHERE id = $id")
        db.close()
    }
}