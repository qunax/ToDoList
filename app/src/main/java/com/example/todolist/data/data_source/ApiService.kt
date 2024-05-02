package com.example.todolist.data.data_source

import android.provider.CalendarContract.Colors
import com.example.todolist.domain.model.ColorsApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val COLOR_API_URL = "https://random-flat-colors.vercel.app/api/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    COLOR_API_URL).build()


interface ApiService{
    @GET("random")
    suspend fun getRandomColors(@Query("count") count: Int) : ColorsApiResponse
}



object TodoApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}