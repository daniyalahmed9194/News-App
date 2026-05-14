package com.example.quiz_2.network

import com.example.quiz_2.model.Country
import retrofit2.http.GET
import retrofit2.http.Query

interface CountriesApiService {
    @GET("all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name,cca2"
    ): List<Country>
}
