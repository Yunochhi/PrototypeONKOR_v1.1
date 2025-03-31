package com.example.prototypeonkor.APIService

import com.example.prototypeonkor.Class.Appointment
import com.example.prototypeonkor.Class.DispensaryObservation
import com.example.prototypeonkor.Class.Notification
import com.example.prototypeonkor.Class.ProtocolInfo
import com.example.prototypeonkor.Class.User
import okhttp3.Response
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


interface PatientApiService {
    //patient
    //protocols
    @POST("patient/listProtocols")
    suspend fun getProtocols(@Body snilsRequest: SnilsRequest): List<ProtocolFile>

    @POST("patient/protocol")
    suspend fun openProtocols(@Body protocolRequest: ProtocolRequest): ResponseBody

    //notification
    @POST("patient/addNotification")
    suspend fun addNotification(@Body notificationRequest: Notification): ResponseBody

    @POST("patient/notifications")
    suspend fun getNotifications(@Body snilsRequest: SnilsRequest): MutableList<Notification>

    //appointments
    @POST("patient/appointments")
    suspend fun getAppointments(@Body snilsRequest: SnilsRequest): List<Appointment>

    //observations
    @POST("patient/observations")
    suspend fun getObservations(@Body snilsRequest: SnilsRequest): List<DispensaryObservation>

    //auth
    @POST("auth/login")
    suspend fun getUserInfo(@Body snilsRequest: SnilsRequest): retrofit2.Response<User>

    @POST("auth/register")
    suspend fun registerUser(@Body user: User): ResponseBody
}