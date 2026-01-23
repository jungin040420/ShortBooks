package com.example.shortbooks

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StarredListAdapter(private val starredList: MutableList<SentenceItem>) :
    RecyclerView.Adapter<StarredListAdapter.ViewHolder>() {

    // 뷰홀더 클래스: 개별 아이템 뷰 구성 요소 참조
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tv_list_content)     // 문장 내용
        val tvBookTitle: TextView = view.findViewById(R.id.tv_list_book_title) // 도서 제목
        val ivStar: ImageView = view.findViewById(R.id.iv_list_star)           // 즐겨찾기 별 아이콘
        val ivBookImage: ImageView = view.findViewById(R.id.iv_list_book_image) // 도서 표지 이미지
    }

    // 아이템 뷰 레이아웃 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_starred_sentence, parent, false)
        return ViewHolder(view)
    }

    // 데이터와 뷰 결합 (바인딩)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = starredList[position]
        val currentPosition = holder.bindingAdapterPosition

        // 1. 텍스트 데이터 바인딩 (문장 내용 및 제목)
        holder.tvContent.text = item.content
        holder.tvBookTitle.text = item.bookTitle

        // 2. 도서 이미지 로드 처리 (Glide 활용)
        holder.ivBookImage.visibility = View.VISIBLE

        if (!item.image.isNullOrBlank()) {
            // 외부 이미지 URL 로드 및 에러 시 기본 이미지 설정
            Glide.with(holder.itemView.context)
                .load(item.image)
                .centerCrop()
                .placeholder(R.drawable.ic_book)
                .error(R.drawable.ic_book)
                .into(holder.ivBookImage)
        } else {
            // 이미지 부재 시 기본 이미지 할당
            holder.ivBookImage.setImageResource(R.drawable.ic_book)
        }

        // 3. 도서 이미지 클릭 이벤트 (구매 페이지 이동)
        holder.ivBookImage.setOnClickListener {
            if (!item.buyLink.isNullOrEmpty()) {
                // 웹 브라우저 실행 인텐트
                val intent = Intent(Intent.ACTION_VIEW, item.buyLink.toUri())
                holder.itemView.context.startActivity(intent)
            }
        }

        // 4. 별 아이콘 클릭 이벤트 (즐겨찾기 해제 및 리스트 삭제)
        holder.ivStar.setOnClickListener {
            val dbHelper = DBHelper(holder.itemView.context)
            // DB 내 즐겨찾기 상태 업데이트 (0: 해제)
            dbHelper.updateStarStatus(item.id, 0)
            // 현재 리스트에서 항목 제거 및 UI 갱신
            starredList.removeAt(currentPosition)
            notifyDataSetChanged()
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount(): Int = starredList.size
}