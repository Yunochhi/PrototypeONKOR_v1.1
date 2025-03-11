package com.example.prototypeonkor.Class

import com.example.prototypeonkor.Enum.AppointmentStatus
import java.time.LocalDate

data class Appointment(
    val LPU: String,
    val investigationName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: AppointmentStatus
)