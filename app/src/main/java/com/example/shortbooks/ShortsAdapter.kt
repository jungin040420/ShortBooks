package com.example.shortbooks

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

/**
 * 문장 수집 콘텐츠 표시 어댑터
 */
class ShortsAdapter(private val list: List<BookItem>, private val db: DBHelper) :
    RecyclerView.Adapter<ShortsAdapter.ViewHolder>() {

    // 뷰 객체 참조 홀더
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tvContent)     // 문장 내용
        val tvComment: TextView = view.findViewById(R.id.tvReview)      // 카드용 한줄평 (ID 유지)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)         // 도서 제목
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)       // 저자 이름
        val btnQuote: ImageButton = view.findViewById(R.id.btnQuote)    // 보기 전환 버튼
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite) // 즐겨찾기 버튼
        val btnBuy: ImageButton = view.findViewById(R.id.btnBuy)         // 구매 연결 버튼
    }

    // 아이템 뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_short, parent, false)
    )

    // 데이터 결합 처리
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // 텍스트 데이터 바인딩
        holder.tvContent.text = item.content
        holder.tvTitle.text = item.title
        holder.tvAuthor.text = item.author
        holder.tvComment.text = item.comment

        // 즐겨찾기 초기 아이콘 설정
        if (item.isFavorite == 1) {
            holder.btnFavorite.setImageResource(R.drawable.ic_blackstar)
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_star)
        }

        // [수정] 따옴표 버튼 클릭 이벤트: 카드용 한줄평(Comment)만 토글
        holder.btnQuote.setOnClickListener {
            // Comment가 숨겨져 있으면 보이게, 보이고 있으면 숨김 처리
            if (holder.tvComment.visibility == View.INVISIBLE) {
                holder.tvComment.visibility = View.VISIBLE
            } else {
                holder.tvComment.visibility = View.INVISIBLE
            }
        }

        // 즐겨찾기 상태 변경 및 DB 업데이트
        holder.btnFavorite.setOnClickListener {
            val newStatus = if (item.isFavorite == 0) 1 else 0
            db.updateFavorite(item.id, newStatus)

            if (newStatus == 1) {
                holder.btnFavorite.setImageResource(R.drawable.ic_blackstar)
            } else {
                holder.btnFavorite.setImageResource(R.drawable.ic_star)
            }
        }

        // 구매 링크 연결
        holder.btnBuy.setOnClickListener {
            if (!item.link.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "구매 링크가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 목록 크기 반환
    override fun getItemCount() = list.size
}