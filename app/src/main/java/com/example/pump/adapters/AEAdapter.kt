package com.example.pump.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.business.ExerciseItem
import com.example.pump.R
import com.example.pump.activity.CreateWorkoutActivity

interface OnExerciseSelectedListener {
    fun onExerciseSelected(exercise: ExerciseItem)
}

private var exerciseSelectedListener: OnExerciseSelectedListener? = null

class AEAdapter(private val activity: Activity, private var exerciseList: List<ExerciseItem>) :
    RecyclerView.Adapter<AEAdapter.ExerciseViewHolder>(), OnExerciseSelectedListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.aecardlayout, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]

        holder.bind(exercise)
    }

    fun setOnExerciseSelectedListener(listener: OnExerciseSelectedListener) {
        exerciseSelectedListener = listener
    }

    override fun getItemCount(): Int = exerciseList.size

    fun updateData(newExercises: List<ExerciseItem>) {
        exerciseList = newExercises
        notifyDataSetChanged()
    }

    override fun onExerciseSelected(exercise: ExerciseItem) {
        // Handle the selected exercise here
        val intent = Intent(activity, CreateWorkoutActivity::class.java)
        val requestCode = 789

        exerciseSelectedListener?.onExerciseSelected(exercise)

        // Pass exercise data as extras
        intent.putExtra("EXTRA_REQUEST_CODE", requestCode)
        intent.putExtra("EXTRA_EXERCISE_NAME", exercise.name)
        intent.putExtra("EXTRA_EXERCISE_TYPE", exercise.type)
        intent.putExtra("EXTRA_EXERCISE_MUSCLE", exercise.muscle)
        intent.putExtra("EXTRA_EXERCISE_EQUIPMENT", exercise.equipment)
        intent.putExtra("EXTRA_EXERCISE_DIFFICULTY", exercise.difficulty)

        // Start the CreateWorkoutActivity with startActivityForResult
        activity.startActivityForResult(intent, requestCode)
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(exercise: ExerciseItem) {
            with(itemView) {

                // Replace the placeholders with the actual IDs from aecardlayout.xml
                val workoutText = findViewById<TextView>(R.id.aeworkout_text)
                val instructionText = findViewById<TextView>(R.id.aeinstruction_text)
                val typeText = findViewById<TextView>(R.id.aetype_text_label)
                val muscleText = findViewById<TextView>(R.id.aemuscle_text_label)
                val equipmentText = findViewById<TextView>(R.id.aequipment_text_label)
                val difficultyText = findViewById<TextView>(R.id.aedifficulty_text_label)

                // Set the text for each TextView
                workoutText.text = exercise.name
                instructionText.text = exercise.instructions
                typeText.text = "Type:\n${exercise.type}"
                muscleText.text = "Muscle:\n${exercise.muscle}"
                equipmentText.text = "Equipment:\n${exercise.equipment}"
                difficultyText.text = "Difficulty:\n${exercise.difficulty}"

                // Access the button and set a click listener
                val addButton = findViewById<Button>(R.id.add_button)
                addButton.setOnClickListener {
                    Log.d("AEAdapter", "Button Clicked")

                    // Call the listener when the button is clicked
                    exerciseSelectedListener?.onExerciseSelected(exercise)
                }

            }
        }
    }
}