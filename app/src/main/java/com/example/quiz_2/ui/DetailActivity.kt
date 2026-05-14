package com.example.quiz_2.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quiz_2.R
import com.example.quiz_2.databinding.ActivityDetailBinding
import com.example.quiz_2.model.Article
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        binding.toolbar.setNavigationOnClickListener { finish() }

        @Suppress("DEPRECATION")
        val article = intent.getSerializableExtra(EXTRA_ARTICLE) as? Article ?: run {
            finish(); return
        }

        binding.txtTitle.text = article.title ?: ""
        binding.txtDescription.text = article.description ?: ""
        binding.txtContent.text = article.content ?: ""
        binding.txtSource.text = article.source?.name ?: ""
        binding.txtDate.text = formatDate(article.publishedAt)

        Glide.with(this)
            .load(article.image)
            .placeholder(R.color.gray_light)
            .error(R.color.gray_light)
            .into(binding.imgNews)

        binding.btnReadFull.setOnClickListener {
            article.url?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
    }

    private fun formatDate(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            val output = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val d = input.parse(raw)
            if (d != null) output.format(d) else raw
        } catch (e: Exception) {
            raw
        }
    }
}
