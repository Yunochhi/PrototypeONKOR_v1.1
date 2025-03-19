package com.example.prototypeonkor.APIService

import com.example.prototypeonkor.Class.Appointment
import com.example.prototypeonkor.Class.DispensaryObservation
import com.example.prototypeonkor.Class.Notification
import com.example.prototypeonkor.Class.ProtocolInfo
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

data class ProtocolFile(
    val fileName: String,
    val info: ProtocolInfo
)

data class SnilsRequest(
    val snils: String
)

data class ProtocolRequest(
    val snils: String,
    val fileName: String
)

data class NotificationRequest(
    val snils: String,
    val notification: Notification
)

interface PatientApiService {
    //protocols
    @POST("listProtocols")
    suspend fun getProtocols(@Body snilsRequest: SnilsRequest): List<ProtocolFile>

    @POST("protocol")
    suspend fun openProtocols(@Body protocolRequest: ProtocolRequest): ResponseBody

    //notification
    @POST("addNotification")
    suspend fun addNotification(@Body notificationRequest: NotificationRequest): ResponseBody

    @POST("notifications")
    suspend fun getNotifications(@Body snilsRequest: SnilsRequest): MutableList<Notification>

    //appointments
    @POST("appointments")
    suspend fun getAppointments(@Body snilsRequest: SnilsRequest): List<Appointment>

    //observations
    @POST("observations")
    suspend fun getObservations(@Body snilsRequest: SnilsRequest): List<DispensaryObservation>


}