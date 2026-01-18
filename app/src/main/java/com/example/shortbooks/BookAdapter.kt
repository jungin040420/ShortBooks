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

        Glide.with(holder.itemView.context).load(book.image).into(holder.image)

        //이미지 클릭 시 문장 추가 화면으로 이동
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddSentenceActivity::class.java)
            intent.putExtra("BOOK_TITLE", book.title)
            intent.putExtra("BOOK_AUTHOR", book.author)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bookList.size

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // [수정] XML에서 지운 TextView 변수를 여기서도 삭제함
        val image: ImageView = itemView.findViewById(R.id.iv_book_cover)
    }
}