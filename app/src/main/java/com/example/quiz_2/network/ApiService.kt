package com.example.quiz_2.network

import com.example.quiz_2.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String = "general",
        @Query("lang") lang: String = "en",
        @Query("country") country: String,
        @Query("max") max: Int = 10,
        @Query("apikey") apiKey: String
    ): NewsResponse
}
