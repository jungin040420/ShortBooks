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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tv_list_content)
        val tvBookTitle: TextView = view.findViewById(R.id.tv_list_book_title)
        val ivStar: ImageView = view.findViewById(R.id.iv_list_star)
        val ivBookImage: ImageView = view.findViewById(R.id.iv_list_book_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_starred_sentence, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = starredList[position]
        val currentPosition = holder.bindingAdapterPosition

        // 1. 텍스트 데이터 연결 (문장, 제목 항상 노출)
        holder.tvContent.text = item.content
        holder.tvBookTitle.text = item.bookTitle


        // 2. 모든 문장에 책 이미지가 보이도록 설정
        holder.ivBookImage.visibility = View.VISIBLE

        if (!item.image.isNullOrBlank()) {
            Glide.with(holder.itemView.context)
                .load(item.image) // DB에 저장된 실제 책 사진 로드
                .centerCrop()
                .placeholder(R.drawable.ic_book)
                .error(R.drawable.ic_book)
                .into(holder.ivBookImage)
        } else {
            holder.ivBookImage.setImageResource(R.drawable.ic_book)
        }

        // 3. 이미지 클릭: 구매 링크 이동
        holder.ivBookImage.setOnClickListener {
            if (!item.buyLink.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, item.buyLink.toUri())
                holder.itemView.context.startActivity(intent)
            }
        }

        // 4. 별 아이콘 클릭: 삭제 처리
        holder.ivStar.setOnClickListener {
            val dbHelper = DBHelper(holder.itemView.context)
            dbHelper.updateStarStatus(item.id, 0)
            starredList.removeAt(currentPosition)
            notifyDataSetChanged() // 중복 체크 재계산을 위해 전체 갱신 권장
        }
    }

    override fun getItemCount(): Int = starredList.size
}