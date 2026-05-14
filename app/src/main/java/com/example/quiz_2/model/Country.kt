package com.example.quiz_2.model

data class CountryName(
    val common: String,
    val official: String
)

data class Country(
    val name: CountryName,
    val cca2: String
)
