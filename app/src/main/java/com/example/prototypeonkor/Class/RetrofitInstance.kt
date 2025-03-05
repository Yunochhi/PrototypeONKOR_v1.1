package com.example.prototypeonkor.Class

import com.example.prototypeonkor.APIService.PatientApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/patient/"

    val apiService: PatientApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PatientApiService::class.java)
    }
}