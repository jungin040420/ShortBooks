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

    // 뷰 객체 참조 홀더 (UI 컴포넌트 연결)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tvContent)     // 문장 내용 텍스트
        val tvComment: TextView = view.findViewById(R.id.tvReview)      // 한줄평 텍스트
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)         // 도서 제목 텍스트
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)       // 저자 이름 텍스트
        val btnQuote: ImageButton = view.findViewById(R.id.btnQuote)    // 코멘트 토글 버튼
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite) // 즐겨찾기 아이콘 버튼
        val btnBuy: ImageButton = view.findViewById(R.id.btnBuy)         // 구매 페이지 연결 버튼
    }

    // 아이템 뷰 레이아웃 생성 및 뷰홀더 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_short, parent, false)
    )

    // 데이터와 뷰홀더 결합 (이벤트 및 데이터 바인딩)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // 텍스트 데이터 UI 반영
        holder.tvContent.text = item.content
        holder.tvTitle.text = item.title
        holder.tvAuthor.text = item.author
        holder.tvComment.text = item.comment

        // 즐겨찾기 상태에 따른 초기 아이콘 설정
        if (item.isFavorite == 1) {
            holder.btnFavorite.setImageResource(R.drawable.ic_blackstar)
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_star)
        }

        // 따옴표 버튼 클릭 이벤트 (한줄평 가시성 토글 제어)
        holder.btnQuote.setOnClickListener {
            // 보임/숨김 상태 반전 처리
            if (holder.tvComment.visibility == View.INVISIBLE) {
                holder.tvComment.visibility = View.VISIBLE
            } else {
                holder.tvComment.visibility = View.INVISIBLE
            }
        }

        // 즐겨찾기 버튼 클릭 이벤트 (DB 연동 및 상태 동기화)
        holder.btnFavorite.setOnClickListener {
            // DB 데이터 업데이트 실행
            val updatedStatus = db.updateStarStatus(item.id)

            // 로컬 데이터 모델 상태 동기화
            item.isFavorite = updatedStatus

            // 변경된 상태에 따른 UI 아이콘 갱신
            if (updatedStatus == 1) {
                holder.btnFavorite.setImageResource(R.drawable.ic_blackstar)
            } else {
                holder.btnFavorite.setImageResource(R.drawable.ic_star)
            }
        }

        // 구매 버튼 클릭 이벤트 (외부 브라우저 연결)
        holder.btnBuy.setOnClickListener {
            if (!item.link.isNullOrEmpty()) {
                // 웹 링크 인텐트 실행
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                holder.itemView.context.startActivity(intent)
            } else {
                // 링크 누락 안내 메시지
                Toast.makeText(holder.itemView.context, "구매 링크가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount() = list.size
}