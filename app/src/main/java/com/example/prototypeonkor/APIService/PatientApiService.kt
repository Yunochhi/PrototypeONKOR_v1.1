package com.example.prototypeonkor.APIService

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

data class ProtocolFile(
    val fileName: String,
    val info: ProtocolInfo
)

data class ProtocolInfo(
    val investigationName : String,
    val doctorName: String,
    val date: String,
    val time: String,
    val lpu: String
)

data class SnilsRequest(
    val snils: String
)

data class Notification(
    val header: String,
    val description: String
)

data class NotificationRequest(val snils: String, val notification: Notification)

interface PatientApiService {
    @POST("listProtocols")
    suspend fun getProtocols(@Body SnilsRequest: SnilsRequest): List<ProtocolFile>

    @POST("addNotification")
    suspend fun addNotification(@Body Request: NotificationRequest)

    @POST("notifications")
    suspend fun getNotifications(@Body SnilsRequest: SnilsRequest): MutableList<Notification>
}