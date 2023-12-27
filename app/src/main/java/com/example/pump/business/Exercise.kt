package com.example.pump.business

data class Exercise(
    val id: Int,
    val uuid: Int,
    val name: String,
    val exercise_base: Int,
    val description: String,
    val created: String,
    val category: Int,
    val muscles: List<Any>,
    val muscles_secondary: List<Any>,
    val equipment: List<Int>,
    val language: Int,
)

