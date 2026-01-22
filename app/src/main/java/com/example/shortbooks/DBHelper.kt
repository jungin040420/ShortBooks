package com.example.shortbooks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// 데이터베이스 관리 클래스
class DBHelper(context: Context) : SQLiteOpenHelper(context, "ShortBook.db", null, 13) {
    // 테이블 생성 루틴
    override fun onCreate(db: SQLiteDatabase) {
        // 도서 및 문장 정보 테이블
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
                "record_date TEXT, " +
                "is_reading INTEGER DEFAULT 0, " +
                "is_favorite INTEGER DEFAULT 0)")

        // 사용자 계정 정보 테이블
        db.execSQL("CREATE TABLE users (email TEXT PRIMARY KEY, pw TEXT, name TEXT)")
    }

    // 데이터베이스 버전 업데이트 처리
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Books")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    /**
     * 사용자 계정 관련 함수 (추가 영역)
     */

    // 회원가입: 유저 정보 저장 (JoinFragment 에러 해결)
    fun insertUser(email: String, pw: String, name: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("pw", pw)
            put("name", name)
        }
        return db.insert("users", null, values) != -1L
    }

    // 로그인: 정보 일치 확인
    fun checkUser(email: String, pw: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND pw=?", arrayOf(email, pw))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // 이름 조회: 이메일로 가입된 이름 가져오기
    fun getUserName(email: String): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM users WHERE email=?", arrayOf(email))
        var name = ""
        if (cursor.moveToFirst()) {
            name = cursor.getString(0)
        }
        cursor.close()
        return name
    }

    // 비밀번호 실제 업데이트 함수
    fun updatePassword(email: String, newPw: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("pw", newPw) // 비밀번호 컬럼 수정
        }

        // 이메일이 일치하는 행의 비밀번호를 변경
        val result = db.update("users", values, "email=?", arrayOf(email))
        db.close()
        return result > 0 // 수정된 행이 있으면 true 반환
    }

    // 기존 비밀번호가 맞는지 확인하는 함수 (변경 전 검증용)
    fun checkCurrentPassword(email: String, currentPw: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND pw=?", arrayOf(email, currentPw))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // 특정 날짜(yyyy-MM-dd)에 문장이 등록되어 있는지 확인하는 함수
    fun hasDataAtDate(date: String): Boolean {
        val db = this.readableDatabase
        // startdate 대신 record_date에서 날짜가 일치하는지 확인
        val cursor = db.rawQuery("SELECT * FROM Books WHERE record_date = ?", arrayOf(date))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    /**
     * 기존 도서 및 문장 관리 함수
     */

    // 문장 수집 데이터 조회 (content 존재 기준)
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

    // 모든 도서 데이터 조회 (중복 제거)
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

    // 커서 데이터를 BookItem 객체로 변환
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

    // 문장 카드 데이터 저장
    fun addCard(content: String, title: String, author: String, link: String, comment: String, image: String, recordDate: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("content", content)
            put("title", title)
            put("author", author)
            put("link", link)
            put("comment", comment)
            put("image", image)
            put("record_date", recordDate)
            put("is_favorite", 0)
        }
        db.insert("Books", null, values)
        db.close()
    }

    // 도서 기록 데이터 저장
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

    // 즐겨찾기 상태 토글 업데이트
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

    // 즐겨찾기 특정 상태로 강제 업데이트
    fun updateStarStatus(id: Int, status: Int) {
        val db = this.writableDatabase
        db.execSQL("UPDATE Books SET is_favorite = $status WHERE id = $id")
        db.close()
    }

    // 전체 문장 리스트 조회 (SentenceItem 변환)
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

    // 즐겨찾기 표시된 문장만 조회
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