package com.example.quiz_2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountriesRetrofitInstance {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    val api: CountriesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApiService::class.java)
    }
}
