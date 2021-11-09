package com.example.nasaapod.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.nasa.gov"
    const val API_KEY = "l9NgLnbFNcpcwzSN9wX42mfoHI5Xtxiv6tTpq52D"
    const val TOTAL = 20

    val api: ClientApi by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientApi::class.java)
    }
}