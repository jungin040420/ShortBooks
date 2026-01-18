package com.example.shortbooks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(private var bookList: List<BookItem>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    fun setData(newList: List<BookItem>) {
        this.bookList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        // 제목 설정 코드를 삭제하여 이미지만 표시
        Glide.with(holder.itemView.context).load(book.image).into(holder.image)
    }

    override fun getItemCount(): Int = bookList.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // [수정] XML에서 지운 TextView 변수를 여기서도 삭제함
        val image: ImageView = itemView.findViewById(R.id.iv_book_cover)
    }
}