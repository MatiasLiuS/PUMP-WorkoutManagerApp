package com.example.pump.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pump.R
import com.example.pump.adapters.SetAdapter
import com.example.pump.business.ExerciseData
import com.example.pump.business.SetData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class CreateWorkoutActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var usernameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var workoutImageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var postButton: Button
    private lateinit var back_button_createworkout: Button
    private lateinit var add_exercise_button: Button
    private lateinit var exercisesContainer: LinearLayout

    private var imageUri: Uri? = null
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val GALLERY_REQUEST_CODE = 123
    private val AE_FILTER_REQUEST_CODE = 456
    private val AE_ACTIVITY_REQUEST_CODE = 789
    private val CS_ACTIVITY_REQUEST_CODE = 321

    private val STATE_IMAGE_URI = "state_image_uri"
    private val STATE_TITLE = "state_title"
    private val exercisesList = mutableListOf<ExerciseData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_workout)
        exercisesContainer = findViewById(R.id.exercisesContainer)

        initUIElements()
        setBackButton()
        setUploadButtonClickListener()
        retrieveUserDetails()
        setPostButtonClickListener()
        setAddExerciseButtonClickListener()

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(STATE_IMAGE_URI)
            titleEditText.setText(savedInstanceState.getString(STATE_TITLE))
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(workoutImageView)
            }
        }
    }

    private fun initUIElements() {
        titleEditText = findViewById(R.id.titleEditText)
        usernameTextView = findViewById(R.id.usernameTextView)
        dateTextView = findViewById(R.id.dateTextView)
        workoutImageView = findViewById(R.id.workoutImageView)
        uploadButton = findViewById(R.id.uploadButton)
        postButton = findViewById(R.id.postButton)
        back_button_createworkout = findViewById(R.id.back_button_createworkout)
        add_exercise_button = findViewById(R.id.add_exercise_button)
    }

    private fun setBackButton() {
        back_button_createworkout.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUploadButtonClickListener() {
        uploadButton.setOnClickListener { openGallery() }
    }

    private fun retrieveUserDetails() {
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val usersRef = FirebaseDatabase.getInstance().getReference("users")

            usersRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val username = dataSnapshot.child("username").getValue(String::class.java)

                        if (username != null) {
                            usernameTextView.text = "@$username"
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })

            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            dateTextView.text = currentDate
        }
    }

    private fun setPostButtonClickListener() {
        postButton.setOnClickListener {
            val title = titleEditText.text.toString()

            if (imageUri != null) {
                uploadImageToFirebaseStorage(title)
            } else {
                Toast.makeText(this, "Please select an image for your workout", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAddExerciseButtonClickListener() {
        add_exercise_button.setOnClickListener {
            val intent = Intent(this, AEFilter::class.java)
            startActivityForResult(intent, AE_FILTER_REQUEST_CODE)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    imageUri = data.data
                    Glide.with(this)
                        .load(imageUri)
                        .into(workoutImageView)
                }
            }
            AE_FILTER_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val exerciseType = data.getStringExtra("EXERCISE_TYPE")
                    val muscleGroup = data.getStringExtra("MUSCLE_GROUP")
                    val difficulty = data.getStringExtra("DIFFICULTY")
                    val searchInput = data.getStringExtra("SEARCH_INPUT")

                    Log.d("CreateWorkoutActivity", "Exercise Type: $exerciseType")
                    Log.d("CreateWorkoutActivity", "Muscle Group: $muscleGroup")
                    Log.d("CreateWorkoutActivity", "Difficulty: $difficulty")
                    Log.d("CreateWorkoutActivity", "Search Input: $searchInput")

                    val intent = Intent(this, AEActivity::class.java)
                    intent.putExtras(data.extras ?: Bundle())
                    startActivityForResult(intent, AE_ACTIVITY_REQUEST_CODE)
                }
            }
            AE_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val exerciseName = data.getStringExtra("EXTRA_EXERCISE_NAME")
                    val exerciseType = data.getStringExtra("EXTRA_EXERCISE_TYPE")
                    val exerciseMuscle = data.getStringExtra("EXTRA_EXERCISE_MUSCLE")
                    val exerciseEquipment = data.getStringExtra("EXTRA_EXERCISE_EQUIPMENT")
                    val exerciseDifficulty = data.getStringExtra("EXTRA_EXERCISE_DIFFICULTY")

                    Log.d("CreateWorkoutActivity", "Exercise Name: $exerciseName")
                    Log.d("CreateWorkoutActivity", "Exercise Type: $exerciseType")
                    Log.d("CreateWorkoutActivity", "Exercise Muscle: $exerciseMuscle")
                    Log.d("CreateWorkoutActivity", "Exercise Equipment: $exerciseEquipment")
                    Log.d("CreateWorkoutActivity", "Exercise Difficulty: $exerciseDifficulty")

                    val csIntent = Intent(this, CSActivity::class.java).apply {
                        putExtra("EXTRA_EXERCISE_NAME", exerciseName)
                        putExtra("EXTRA_EXERCISE_TYPE", exerciseType)
                        putExtra("EXTRA_EXERCISE_MUSCLE", exerciseMuscle)
                        putExtra("EXTRA_EXERCISE_EQUIPMENT", exerciseEquipment)
                        putExtra("EXTRA_EXERCISE_DIFFICULTY", exerciseDifficulty)
                    }
                    startActivityForResult(csIntent, CS_ACTIVITY_REQUEST_CODE)
                }
            }
            CS_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val exerciseName = data.getStringExtra("EXTRA_EXERCISE_NAME") ?: ""
                    val exerciseType = data.getStringExtra("EXTRA_EXERCISE_TYPE") ?: ""
                    val exerciseMuscle = data.getStringExtra("EXTRA_EXERCISE_MUSCLE") ?: ""
                    val exerciseEquipment = data.getStringExtra("EXTRA_EXERCISE_EQUIPMENT") ?: ""
                    val exerciseDifficulty = data.getStringExtra("EXTRA_EXERCISE_DIFFICULTY") ?: ""

                    Log.d("CreateWorkoutActivity", "Exercise Name: $exerciseName")
                    Log.d("CreateWorkoutActivity", "Exercise Type: $exerciseType")
                    Log.d("CreateWorkoutActivity", "Exercise Muscle: $exerciseMuscle")
                    Log.d("CreateWorkoutActivity", "Exercise Equipment: $exerciseEquipment")
                    Log.d("CreateWorkoutActivity", "Exercise Difficulty: $exerciseDifficulty")

                    val sets = data.getSerializableExtra("EXTRA_SETS") as List<SetAdapter.CardData>
                    sets.forEachIndexed { index, set ->
                        Log.d("CreateWorkoutActivity", "SET_${index + 1}_SET_NUMBER: ${index + 1}")
                        Log.d("CreateWorkoutActivity", "SET_${index + 1}_REPS: ${set.reps}")
                        Log.d("CreateWorkoutActivity", "SET_${index + 1}_WEIGHT: ${set.weight}")
                    }

                    val exerciseData = ExerciseData(
                        name = exerciseName,
                        type = exerciseType,
                        muscle = exerciseMuscle,
                        equipment = exerciseEquipment,
                        difficulty = exerciseDifficulty,
                        sets = sets.map { SetData(it.weight, it.reps) }
                    )

                    exercisesList.add(exerciseData)

                    val exerciseItemView = layoutInflater.inflate(R.layout.exercise_item, null)
                    exerciseItemView.findViewById<TextView>(R.id.exerciseNameTextView).text = exerciseName

                    exercisesContainer.addView(exerciseItemView)
                    Log.d("CreateWorkoutActivity", "Added Exercise: $exerciseData")
                    Log.d("CreateWorkoutActivity", "Ex List $exercisesList")
                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage(title: String) {
        val storageRef = storage.reference
        val imageRef = storageRef.child("workout_images/${UUID.randomUUID()}.jpg")

        if (imageUri != null) {
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveWorkoutToDatabase(title, uri.toString(), exercisesList)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Upload failed: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveWorkoutToDatabase(title: String, imageUrl: String, exercises: List<ExerciseData>) {
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val usersRef = FirebaseDatabase.getInstance().getReference("users")
            val workoutsRef = usersRef.child(uid).child("workouts")
            val workoutKey = workoutsRef.push().key

            if (workoutKey != null) {
                val workout = Workout(title, imageUrl, dateTextView.text.toString(), exercises)

                workoutsRef.child(workoutKey).setValue(workout)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Workout posted successfully!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to post workout. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(STATE_IMAGE_URI, imageUri)
        outState.putString(STATE_TITLE, titleEditText.text.toString())
    }

    data class Workout(val title: String, val imageUrl: String, val date: String, val exercises: List<ExerciseData>)
}
