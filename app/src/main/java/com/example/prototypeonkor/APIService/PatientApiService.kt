package com.example.prototypeonkor.APIService

import com.example.prototypeonkor.Classes.Appointment
import com.example.prototypeonkor.Classes.Chat.Chat
import com.example.prototypeonkor.Classes.Chat.Message
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.Disease
import com.example.prototypeonkor.Classes.DispensaryObservation
import com.example.prototypeonkor.Classes.Notification
import com.example.prototypeonkor.Classes.ProtocolInfo
import com.example.prototypeonkor.Classes.Requests.ProtocolRequest
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.Classes.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class ProtocolFile(
    val fileName: String,
    val info: ProtocolInfo
)

data class MkbCodeRequest(
    val mkbCode: String
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
    //login
    @POST("auth/login")
    suspend fun getUserInfo(@Body snilsRequest: SnilsRequest): Response<User>

    //register
    @POST("auth/register")
    suspend fun registerUser(@Body user: User): ResponseBody

    //diseases
    @POST("diseases/get_or")
    suspend fun getDisease(@Body request: MkbCodeRequest): Response<Disease>

    //chat
    @POST("chat/user/{userId}")
    suspend fun createChat(@Path("userId") userId: Long): Response<Chat>

    @GET("chat/{chatId}/messages")
    suspend fun getChatMessages(@Path("chatId") chatId: Long): Response<List<Message>>

    @GET("chat/user/{userId}/active")
    fun getUserActiveChats(@Path("userId") userId: Long): Response<List<Chat>>
}