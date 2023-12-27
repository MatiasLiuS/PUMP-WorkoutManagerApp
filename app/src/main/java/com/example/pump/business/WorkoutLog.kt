package com.example.pump.business

data class WorkoutLog(
    val name: String,
    val date: String,
    val exercises: List<Exercise>,
)

data class Exercises(
    val exerciseName: String,
    val sets: List<Set>,
)

data class Set(
    val reps: Int,
    val weight: Double,
)
