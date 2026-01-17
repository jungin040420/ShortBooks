package com.example.shortbooks

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ShortsAdapter(private val list: List<BookItem>, private val db: DBHelper) : RecyclerView.Adapter<ShortsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val tvReview: TextView = view.findViewById(R.id.tvReview) // 한줄평 연결
        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        val btnQuote: ImageButton = view.findViewById(R.id.btnQuote)
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite)
        val btnBuy: Button = view.findViewById(R.id.btnBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_short, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvContent.text = item.content
        holder.tvReview.text = item.review
        holder.tvInfo.text = "${item.title} - ${item.author}"

        holder.btnFavorite.setImageResource(if(item.isFavorite == 1) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)

        // 따옴표 클릭 시 한줄평 보이기/가리기
        holder.btnQuote.setOnClickListener {
            holder.tvReview.visibility = if(holder.tvReview.visibility == View.INVISIBLE) View.VISIBLE else View.INVISIBLE
        }

        holder.btnFavorite.setOnClickListener {
            item.isFavorite = if(item.isFavorite == 0) 1 else 0
            db.updateFavorite(item.id, item.isFavorite)
            holder.btnFavorite.setImageResource(if(item.isFavorite == 1) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
        }

        holder.btnBuy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = list.size
}