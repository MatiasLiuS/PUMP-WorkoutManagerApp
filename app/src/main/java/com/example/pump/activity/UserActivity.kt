package com.example.pump.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.adapters.WorkoutAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.pump.business.Workout
import com.example.pump.business.User
import java.text.SimpleDateFormat
import java.util.Locale

class UserActivity : AppCompatActivity(), WorkoutAdapter.OnItemClickListener {

    // Firebase components
    private lateinit var usernameText: TextView
    private lateinit var uidText: TextView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null // Declare userId as a nullable property

    // Workout data
    private val workoutList = mutableListOf<Workout>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Buttons in the layout
        val profileButton: Button = findViewById(R.id.profile_button)
        val workoutsButton: Button = findViewById(R.id.workouts_button)
        val settingsButton: Button = findViewById(R.id.settings_button)
        val postButton: Button = findViewById(R.id.post_button)

        // Initialize TextViews after setContentView
        usernameText = findViewById(R.id.username_text)
        uidText = findViewById(R.id.uid_text)

        // Get current user UID
        val currentUserUid = auth.currentUser?.uid
        userId = auth.currentUser?.uid

        // Check if UID is not null
        if (currentUserUid != null) {
            // Fetch user data from Firebase
            database.child("users").child(currentUserUid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    // Check if user data exists
                    if (userSnapshot.exists()) {
                        // Get username and UID from the user snapshot
                        val username = userSnapshot.child("username").value.toString()
                        val uid = userSnapshot.key.toString()

                        // Set username and UID to TextViews
                        usernameText.text = "@$username"
                        uidText.text = "UID: $uid"

                        // Clear existing workoutList before fetching new data
                        workoutList.clear()


                        val workoutsSnapshot = userSnapshot.child("workouts")

                        val sortedWorkouts = workoutsSnapshot.children.sortedByDescending {
                            val dateString = it.child("date").value.toString()
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                            val date = dateFormat.parse(dateString)
                            date?.time ?: 0L
                        }



                        for (workoutSnapshot in sortedWorkouts) {
                            // Get workout details
                            val workoutDate = workoutSnapshot.child("date").value.toString()
                            val imageUrl = workoutSnapshot.child("imageUrl").value.toString()
                            val workoutTitle = workoutSnapshot.child("title").value.toString()
                            val workoutId = workoutSnapshot.key.toString()

                            // Get user details
                            val userId = userSnapshot.key.toString() // Assuming user ID is the same as in the workouts node
                            val username = userSnapshot.child("username").value.toString()

                            // Create a User object
                            val user = User(userId, username)

                            // Create a list of exercises (replace with your actual data structure)
                            val exercisesList = mutableListOf<String>() // Update the type based on your actual structure
                            val exercisesSnapshot = workoutSnapshot.child("exercises")

                            for (exerciseSnapshot in exercisesSnapshot.children) {
                                val exerciseName = exerciseSnapshot.child("name").value.toString()
                                exercisesList.add(exerciseName)
                            }

                            // Create a Workout object with user details and the list of exercises
                            val workout = Workout(workoutId, workoutDate, imageUrl, workoutTitle, user, exercisesList)

                            // Add the workout to the list
                            workoutList.add(workout)
                        }

                        // Notify the adapter that the data has changed
                        adapter.updateWorkoutList(workoutList)

                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }

// Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.workout_recycler)
        adapter = WorkoutAdapter(workoutList, this) // Pass the OnItemClickListener

// Set up RecyclerView with a LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

// Set click listener for RecyclerView items
        adapter.setOnItemClickListener(this)  // Ensure this line is present

// Fetch workout data from Firebase or any other data source



        // Set click listeners for each button
        profileButton.setOnClickListener { profileButtonClick() }
        workoutsButton.setOnClickListener { workoutsButtonClick() }
        settingsButton.setOnClickListener { settingsButtonClick() }
        postButton.setOnClickListener { startCreateWorkoutActivity() }

        // Fetch workout data from Firebase or any other data source
    }

    // Function to load the MainActivity
    private fun loadMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    // Function to handle the profile button click
    private fun profileButtonClick() {
        // Navigate to the UserActivity or replace with the appropriate activity
        startActivity(Intent(this, UserActivity::class.java))
    }

    // Function to handle the workouts button click
    private fun workoutsButtonClick() {
        // Navigate to AWFilter or replace with the appropriate activity
        startActivity(Intent(this, AWFilter::class.java))
    }

    // Function to handle the settings button click
    private fun settingsButtonClick() {
        startActivity(Intent(this, EditProfileActivity::class.java))
    }

    // Function to start the CreateWorkoutActivity
    private fun startCreateWorkoutActivity() {
        startActivity(Intent(this, CreateWorkoutActivity::class.java))
    }

    override fun onItemClick(workout: Workout) {
        // Pass the workout ID to WDActivity
        val intent = Intent(this@UserActivity, WDActivity::class.java)
        intent.putExtra("workoutId", workout.id)

        // Log the workout details
        intent.putExtra("userId", userId) // Make sure userId is available in this scope
        Log.d("UserActivity", "Workout ID: ${workout.id}")

        // Start the WDActivity
        startActivity(intent)
        Log.d("UserActivity", "Tried to open but it did not work")
    }

}

