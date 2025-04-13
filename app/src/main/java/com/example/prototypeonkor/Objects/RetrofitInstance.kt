package com.example.prototypeonkor.Objects

import com.example.prototypeonkor.APIService.PatientApiService
import com.example.prototypeonkor.Classes.Serializers.LocalDateTimeDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
        .create()

    val apiService: PatientApiService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build().create(PatientApiService::class.java)
    }
}