package com.example.shortbooks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodayCardAdapter(private val list: List<SentenceItem>) :
    RecyclerView.Adapter<TodayCardAdapter.ViewHolder>() {

    // 뷰홀더 클래스: 카드 아이템의 UI 구성 요소 참조
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tv_card_content)     // 문장 내용 텍스트
        val tvBookTitle: TextView = view.findViewById(R.id.tv_card_book_title) // 도서 제목 텍스트
        val tvAuthor: TextView = view.findViewById(R.id.tv_card_author)       // 저자 이름 텍스트
        val ivStar: ImageView = view.findViewById(R.id.iv_card_star)           // 즐겨찾기 별 아이콘
    }

    // 아이템 뷰 레이아웃 인플레이트 및 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sentence_card, parent, false)
        return ViewHolder(view)
    }

    // 데이터 바인딩 및 이벤트 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 현재 아이템의 최신 위치 및 데이터 참조
        val currentPosition = holder.bindingAdapterPosition
        val item = list[currentPosition]

        // 텍스트 데이터 UI 주입
        holder.tvContent.text = item.content
        holder.tvBookTitle.text = item.bookTitle
        holder.tvAuthor.text = item.author

        // 즐겨찾기 상태에 따른 별 아이콘 초기화
        if (item.isStarred == 1) {
            holder.ivStar.setImageResource(R.drawable.ic_blackstar) // 활성 상태
        } else {
            holder.ivStar.setImageResource(R.drawable.ic_star)      // 비활성 상태
        }

        // 별 아이콘 클릭 이벤트 (상태 토글 및 DB 반영)
        holder.ivStar.setOnClickListener {
            val dbHelper = DBHelper(holder.itemView.context)

            // 즐겨찾기 상태값 반전 처리 (1 <-> 0)
            val newStatus = if (item.isStarred == 1) 0 else 1

            // DB 내 즐겨찾기 데이터 업데이트 실행
            dbHelper.updateStarStatus(item.id, newStatus)

            // 로컬 데이터 모델 값 동기화
            item.isStarred = newStatus

            // 변경된 아이템 위치의 UI만 부분 갱신
            notifyItemChanged(currentPosition)
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount() = list.size
}