package com.example.nasaapod.network

import com.example.nasaapod.model.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientApi {
    @GET("/planetary/apod")
    suspend fun getPhotos(
        @Query("api_key") key: String,
        @Query("count") number: Int
    ): retrofit2.Response<List<Photo>>
}