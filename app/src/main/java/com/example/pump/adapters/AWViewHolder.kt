package com.example.pump.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.manager.Exercise

// ViewHolder class for holding views of individual exercises in a RecyclerView.
class AWViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val workoutText: TextView = itemView.findViewById(R.id.workout_text)
    private val instructionsText: TextView = itemView.findViewById(R.id.instructions_text)
    private val categoryText: TextView = itemView.findViewById(R.id.category_text)
    private val muscleText: TextView = itemView.findViewById(R.id.muscle_text)
    private val equipmentText: TextView = itemView.findViewById(R.id.equipment_text)

    // Binds exercise data to the views in the ViewHolder.
    fun bind(exercise: Exercise) {
        workoutText.text = exercise.name
        instructionsText.text = "${exercise.description}"
        categoryText.text = "Category: ${exercise.categoryName}"
        muscleText.text = "Muscles: ${exercise.muscleNames.joinToString(", ")}"
        equipmentText.text = "Equipment: ${exercise.equipmentNames.joinToString(", ")}"
    }
}
