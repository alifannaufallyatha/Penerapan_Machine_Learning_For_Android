package com.dicoding.asclepius.database.remote.retrofit

import com.dicoding.asclepius.database.remote.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?q=cancer")
    fun getNews(@Query("apiKey") apiKey: String): Call<NewsResponse>
}