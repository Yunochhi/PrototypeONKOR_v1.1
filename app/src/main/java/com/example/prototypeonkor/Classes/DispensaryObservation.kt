package com.example.prototypeonkor.Classes


data class DispensaryObservation(
    val LPU: String,
    val nextAppointmentDate: String,
    val doctorName: String,
    val disease: String,
    val mkbCodes: String,
    val diseaseDetails: Disease? = null
)