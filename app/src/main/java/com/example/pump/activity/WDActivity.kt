package com.example.pump.activity

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.adapters.ExerciseAdapter
import com.example.pump.business.ExerciseData
import com.example.pump.business.WorkoutData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WDActivity : AppCompatActivity(), ExerciseAdapter.OnItemClickListener {
    private lateinit var workoutTitleTextView: TextView
    private lateinit var workoutId: String
    private lateinit var userId: String
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wdactivity)

        workoutTitleTextView = findViewById(R.id.workoutTitleTextView)

        // Retrieve the workoutId and userId from the Intent
        workoutId = intent.getStringExtra("workoutId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""
        Log.d("WDActivity", "Received userId: $userId, workoutId: $workoutId")

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users/$userId/workouts")

        // Fetch workout data based on workoutId
        fetchWorkoutData()
    }

    private fun fetchWorkoutData() {
        databaseReference.child(workoutId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val workoutData = dataSnapshot.getValue(WorkoutData::class.java)

                    // Log all data for the workout
                    Log.d("WDActivity", "Workout Title: ${workoutData?.title}")
                    Log.d("WDActivity", "Workout Image URL: ${workoutData?.imageUrl}")
                    Log.d("WDActivity", "Workout Date: ${workoutData?.date}")

                    val exercises = workoutData?.exercises
                    if (exercises != null && exercises.isNotEmpty()) {
                        val recyclerView: RecyclerView = findViewById(R.id.exerciseRecyclerView)
                        val exerciseAdapter = ExerciseAdapter(exercises, this@WDActivity) // Pass the click listener
                        recyclerView.adapter = exerciseAdapter
                        recyclerView.layoutManager = LinearLayoutManager(this@WDActivity) // LinearLayoutManager or GridLayoutManager as per your requirement
                    }

                    // Continue updating other UI elements or performing other actions
                } else {
                    Log.e("WDActivity", "No data found for workoutId: $workoutId")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("WDActivity", "Database error: ${databaseError.message}")
            }
        })
    }

    // Handle the click event for each exercise
    override fun onItemClick(exercise: ExerciseData) {
        // Implement the desired action when a card is clicked
        // For example, you can show additional details or navigate to a new activity

        val exerciseName = exercise.name
        val exerciseSets = exercise.sets

        // Create an intent to start SDActivity
        val intent = Intent(this@WDActivity, SDActivity::class.java)

        // Pass the exercise name and sets as extras
        intent.putExtra("exerciseName", exerciseName)
        intent.putExtra("exerciseSets", ArrayList(exerciseSets)) // Convert to ArrayList for Serializable

        // Start the SDActivity with the intent
        startActivity(intent)
    }


}

