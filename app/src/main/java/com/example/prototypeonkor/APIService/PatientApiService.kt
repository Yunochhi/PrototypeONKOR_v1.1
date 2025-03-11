package com.example.prototypeonkor.APIService

import com.example.prototypeonkor.Class.Appointment
import com.example.prototypeonkor.Class.Notification
import retrofit2.http.Body
import retrofit2.http.POST

data class ProtocolFile(
    val fileName: String,
    val info: ProtocolInfo
)

data class ProtocolInfo(
    val investigationName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val lpu: String
)

data class SnilsRequest(
    val snils: String
)

data class NotificationRequest(
    val snils: String,
    val notification: Notification
)

interface PatientApiService
{
    @POST("listProtocols")
    suspend fun getProtocols(@Body snilsRequest: SnilsRequest): List<ProtocolFile>

    @POST("addNotification")
    suspend fun addNotification(@Body notificationRequest: NotificationRequest)

    @POST("notifications")
    suspend fun getNotifications(@Body snilsRequest: SnilsRequest): MutableList<Notification>

    @POST("appointments")
    suspend fun getAppointments(@Body snilsRequest: SnilsRequest): List<Appointment>
}
