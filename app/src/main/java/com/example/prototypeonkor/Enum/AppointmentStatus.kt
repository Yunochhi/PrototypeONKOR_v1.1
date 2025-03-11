package com.example.prototypeonkor.Enum

import kotlinx.serialization.Serializable

@Serializable
enum class AppointmentStatus {
    PLANNED,
    MISSED,
    COMPLETED
}