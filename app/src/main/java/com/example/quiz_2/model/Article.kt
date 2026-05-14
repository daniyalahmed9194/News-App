package com.example.quiz_2.model

import java.io.Serializable

data class Article(
    val title: String?,
    val description: String?,
    val content: String?,
    val url: String?,
    val image: String?,
    val publishedAt: String?,
    val source: Source?
) : Serializable
