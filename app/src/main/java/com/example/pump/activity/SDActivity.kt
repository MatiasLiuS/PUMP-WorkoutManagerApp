package com.example.pump.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pump.R
import com.example.pump.adapters.SDAdapter
import com.example.pump.adapters.SetAdapter
import com.example.pump.business.SetData

class SDActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdactivity)

        // Retrieve exercise name and sets from the intent extras
        val exerciseName = intent.getStringExtra("exerciseName")
        val exerciseSets = intent.getSerializableExtra("exerciseSets") as ArrayList<SetData>?

        // Update UI with exercise name
        val exerciseTitleTextView: TextView = findViewById(R.id.exerciseTitleTextView)
        exerciseTitleTextView.text = exerciseName

        // Set up RecyclerView for sets
        val setsRecyclerView: RecyclerView = findViewById(R.id.setsRecyclerView)
        setsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Create and set adapter for sets
        // Check if exerciseSets is not null before creating the adapter
        if (exerciseSets != null) {
            // Set up RecyclerView for sets
            val setsRecyclerView: RecyclerView = findViewById(R.id.setsRecyclerView)
            setsRecyclerView.layoutManager = LinearLayoutManager(this)

            // Create and set adapter for sets
            val setAdapter = SDAdapter(exerciseSets)
            setsRecyclerView.adapter = setAdapter
        } else {
            // Handle the case where exerciseSets is null (e.g., show a message or take appropriate action)
            // You can add your logic here based on your requirements
        }
    }
}
