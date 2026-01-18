package com.example.shortbooks

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

/**
 * 책의 요약 내용(Shorts) 리스트를 화면에 표시하기 위한 어댑터
 * @param list 표시할 책 데이터 리스트
 * @param db 즐겨찾기 상태를 저장하기 위한 데이터베이스 헬퍼 객체
 */
class ShortsAdapter(private val list: List<BookItem>, private val db: DBHelper) : RecyclerView.Adapter<ShortsAdapter.ViewHolder>() {

    // 각 아이템 뷰의 구성 요소들을 저장하는 홀더 클래스
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tvContent)   // 책 본문 요약
        val tvReview: TextView = view.findViewById(R.id.tvReview)     // 한줄평(기본 invisible)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)       // 책 제목
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)     // 저자 이름
        val btnQuote: ImageButton = view.findViewById(R.id.btnQuote)   // 한줄평 보기 버튼
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite) // 즐겨찾기(별) 버튼
        val btnBuy: ImageButton = view.findViewById(R.id.btnBuy)       // 구매 링크 버튼
    }

    // 레이아웃 파일을 인플레이트하여 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_short, parent, false)
    )

    // 각 위치(position)의 데이터를 뷰에 결합(Binding)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // 1. 텍스트 데이터 설정: UI 요소에 책 정보 반영
        holder.tvContent.text = item.content
        holder.tvReview.text = item.review
        holder.tvTitle.text = item.title
        holder.tvAuthor.text = item.author

        // 2. 초기 즐겨찾기 상태 설정: DB 값(0 또는 1)에 따라 별 모양 아이콘 설정
        if (item.isFavorite == 1) {
            holder.btnFavorite.setImageResource(R.drawable.ic_blackstar) // 채워진 별
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_star)      // 빈 별
        }

        // 3. 한줄평(따옴표 버튼) 클릭 이벤트: 한줄평 레이아웃을 보이거나 숨김(Toggle)
        holder.btnQuote.setOnClickListener {
            if (holder.tvReview.visibility == View.INVISIBLE) {
                holder.tvReview.visibility = View.VISIBLE
            } else {
                holder.tvReview.visibility = View.INVISIBLE
            }
        }

        // 4. 즐겨찾기 버튼 클릭 이벤트: 클릭 시 상태 전환(0<->1), DB 업데이트 및 아이콘 교체
        holder.btnFavorite.setOnClickListener {
            item.isFavorite = if (item.isFavorite == 0) 1 else 0 // 상태 반전
            db.updateFavorite(item.id, item.isFavorite)          // 로컬 DB 반영

            if (item.isFavorite == 1) {
                holder.btnFavorite.setImageResource(R.drawable.ic_blackstar)
            } else {
                holder.btnFavorite.setImageResource(R.drawable.ic_star)
            }
        }

        // 5. 구매 아이콘 클릭 이벤트: 외부 브라우저를 통해 책 구매 링크로 이동
        holder.btnBuy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            holder.itemView.context.startActivity(intent)
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount() = list.size
}