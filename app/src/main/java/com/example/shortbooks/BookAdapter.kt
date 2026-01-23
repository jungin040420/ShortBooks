package com.example.shortbooks

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(private var bookList: List<BookItem>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // 데이터 갱신 및 UI 리프레시
    fun setData(newList: List<BookItem>) {
        this.bookList = newList
        notifyDataSetChanged()
    }

    // 아이템 뷰 생성을 위한 뷰홀더 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    // 뷰홀더와 실제 데이터 바인딩
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]

        // Glide를 이용한 이미지 로딩
        Glide.with(holder.itemView.context).load(book.image).into(holder.image)

        // 아이템 클릭 이벤트 (문장 추가 화면 전환 및 데이터 전달)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddSentenceActivity::class.java)

            // 인텐트에 도서 정보 첨부
            intent.putExtra("BOOK_TITLE", book.title)
            intent.putExtra("BOOK_AUTHOR", book.author)
            intent.putExtra("BOOK_IMAGE", book.image)
            intent.putExtra("BOOK_LINK", book.link)

            // 액티비티 시작
            holder.itemView.context.startActivity(intent)
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount(): Int = bookList.size

    // 내부 클래스: 개별 아이템 뷰 참조 관리
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_book_cover)
    }
}