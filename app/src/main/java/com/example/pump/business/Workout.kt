package com.example.pump.business

import java.io.Serializable

data class Workout(
    val id: String,
    val date: String,
    val imageUrl: String,
    val title: String,
    val user: User,
    val exercises: List<String>
): Serializable



