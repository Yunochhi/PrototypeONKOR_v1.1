package com.example.prototypeonkor.APIService

import retrofit2.http.Body
import retrofit2.http.POST

data class ProtocolFile(
    val fileName: String,
    val info: ProtocolInfo
)

data class ProtocolInfo(
    val doctorName: String,
    val date: String,
    val time: String,
    val lpu: String
)

data class SnilsRequest(
    val snils: String
)

interface PatientApiService {
    @POST("listProtocols")
    suspend fun getProtocols(@Body snilsRequest: SnilsRequest): List<ProtocolFile>
}