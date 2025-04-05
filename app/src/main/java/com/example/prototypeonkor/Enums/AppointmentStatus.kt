package com.example.prototypeonkor.Enums

import kotlinx.serialization.Serializable

@Serializable
enum class AppointmentStatus {
    PLANNED,
    MISSED,
    COMPLETED
}