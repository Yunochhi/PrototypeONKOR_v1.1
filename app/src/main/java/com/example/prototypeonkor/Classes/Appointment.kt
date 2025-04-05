package com.example.prototypeonkor.Classes

import com.example.prototypeonkor.Enums.AppointmentStatus

data class Appointment(
    val LPU: String,
    val investigationName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: AppointmentStatus
)