package com.example.quiz_2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quiz_2.R
import com.example.quiz_2.model.Article
import com.example.quiz_2.ui.DetailActivity
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(
    private var articles: List<Article>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    private val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imgNews)
        val title: TextView = itemView.findViewById(R.id.txtTitle)
        val source: TextView = itemView.findViewById(R.id.txtSource)
        val date: TextView = itemView.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title ?: ""
        holder.source.text = article.source?.name ?: ""
        holder.date.text = formatDate(article.publishedAt)

        Glide.with(holder.itemView.context)
            .load(article.image)
            .placeholder(R.color.gray_light)
            .error(R.color.gray_light)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val ctx = holder.itemView.context
            val intent = Intent(ctx, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ARTICLE, article)
            }
            ctx.startActivity(intent)
        }
    }

    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    private fun formatDate(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return try {
            val date = inputFormat.parse(raw)
            if (date != null) outputFormat.format(date) else raw
        } catch (e: Exception) {
            raw
        }
    }
}
