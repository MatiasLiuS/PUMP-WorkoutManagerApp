package com.example.pump.business

class Filters {
    data class Category(val id: Int, val name: String)
    data class Muscle(val id: Int, val name_en: String)
    data class Equipment(val id: Int, val name: String)

    companion object {

        val Categories: List<Category> = listOf(
            Category(0, "N/A"),
            Category(10, "Abs"),
            Category(8, "Arms"),
            Category(12, "Back"),
            Category(14, "Calves"),
            Category(15, "Cardio"),
            Category(11, "Chest"),
            Category(9, "Legs"),
            Category(13, "Shoulders")
        )

        val Muscles: List<Muscle> = listOf(
            Muscle(0, "N/A"),
            Muscle(2, "Shoulders"),
            Muscle(1, "Biceps"),
            Muscle(11, "Hamstrings"),
            Muscle(13, "Brachialis"),
            Muscle(7, "Calves"),
            Muscle(8, "Glutes"),
            Muscle(12, "Lats"),
            Muscle(14, "Obliquus externus abdominis"),
            Muscle(4, "Chest"),
            Muscle(10, "Quads"),
            Muscle(6, "Abs"),
            Muscle(3, "Serratus anterior"),
            Muscle(15, "Soleus"),
            Muscle(9, "Trapezius"),
            Muscle(5, "Triceps")
        )

        val Equipments: List<Equipment> = listOf(
            Equipment(0, "N/A"),
            Equipment(1, "Barbell"),
            Equipment(8, "Bench"),
            Equipment(3, "Dumbbell"),
            Equipment(4, "Gym mat"),
            Equipment(9, "Incline bench"),
            Equipment(10, "Kettlebell"),
            Equipment(7, "none (bodyweight exercise)"),
            Equipment(6, "Pull-up bar"),
            Equipment(5, "Swiss Ball"),
            Equipment(2, "SZ-Bar")
        )
    }
}
