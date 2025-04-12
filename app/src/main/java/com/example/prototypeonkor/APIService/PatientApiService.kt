package com.example.prototypeonkor.APIService

import com.example.prototypeonkor.Classes.Appointment
import com.example.prototypeonkor.Classes.Chat.Chat
import com.example.prototypeonkor.Classes.Chat.Message
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.DispensaryObservation
import com.example.prototypeonkor.Classes.Notification
import com.example.prototypeonkor.Classes.ProtocolInfo
import com.example.prototypeonkor.Classes.User
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    //chat
    @POST("chat/user/{userId}")
    suspend fun createChat(@Path("userId") userId: Long): retrofit2.Response<Chat>

    @GET("chat/{chatId}/messages")
    suspend fun getChatMessages(@Path("userId") chatId: Long): retrofit2.Response<List<Message>>

    @POST("chat/sendMessage")
    fun sendMessage(@Body chatMessage: MessageDTO): ResponseBody
}