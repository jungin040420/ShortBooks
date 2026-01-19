package com.example.shortbooks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodayCardAdapter(private val list: List<SentenceItem>) :
    RecyclerView.Adapter<TodayCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // XML 아이디 매칭
        val tvContent: TextView = view.findViewById(R.id.tv_card_content)
        val tvBookTitle: TextView = view.findViewById(R.id.tv_card_book_title)
        val tvAuthor: TextView = view.findViewById(R.id.tv_card_author)
        val ivStar: ImageView = view.findViewById(R.id.iv_card_star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sentence_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPosition = holder.bindingAdapterPosition // 최신 인덱스 참조 방식
        val item = list[currentPosition]

        // 텍스트 데이터 주입
        holder.tvContent.text = item.content
        holder.tvBookTitle.text = item.bookTitle
        holder.tvAuthor.text = item.author

        // 별표 아이콘 초기 설정: 상태 값에 따라 분기
        if (item.isStarred == 1) {
            holder.ivStar.setImageResource(R.drawable.ic_blackstar) // 채워진 별
        } else {
            holder.ivStar.setImageResource(R.drawable.ic_star) // 빈 별
        }

        // 별표 클릭 시 즉시 상태 변경 로직
        holder.ivStar.setOnClickListener {
            val dbHelper = DBHelper(holder.itemView.context)

            // 상태값 반전 (1이면 0으로, 0이면 1로)
            val newStatus = if (item.isStarred == 1) 0 else 1

            // DB 데이터 업데이트
            dbHelper.updateStarStatus(item.id, newStatus)

            // 메모리 내 데이터 값 즉시 수정
            item.isStarred = newStatus

            // 해당 아이템 위치만 새로고침 지시
            notifyItemChanged(currentPosition)
        }
    }

    override fun getItemCount() = list.size
}