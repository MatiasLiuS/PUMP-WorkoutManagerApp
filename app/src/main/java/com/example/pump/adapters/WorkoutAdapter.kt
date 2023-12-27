package com.example.pump.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pump.R
import com.example.pump.business.Workout

// Adapter class for displaying a list of workouts in a RecyclerView.
class WorkoutAdapter(
    private var workoutList: List<Workout>,
    private var listener: OnItemClickListener? = null
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    // ViewHolder class representing the individual item view in the RecyclerView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.date_card_text)
        val thumbnail: ImageView = itemView.findViewById(R.id.Thumbnail)
        val titleTextView: TextView = itemView.findViewById(R.id.exercise_name_card_text)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernam_card_text)
        val exercisesContainer: LinearLayout = itemView.findViewById(R.id.workouts_chip_group)
    }

    interface OnItemClickListener {
        fun onItemClick(workout: Workout)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    // Creates a new ViewHolder instance.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.workout_card_layout, parent, false)
        return ViewHolder(view)
    }

    // Binds the data at the specified position in the workoutList to the views in the ViewHolder.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workoutList[position]

        // Set date and title information
        holder.dateTextView.text = "Date: ${workout.date}"
        holder.titleTextView.text = workout.title

        // Access user information directly from the Workout object
        val username = workout.user.username
        holder.usernameTextView.text = "@$username"

        // Load image using Glide (make sure to add the appropriate dependencies)
        Glide.with(holder.itemView.context)
            .load(workout.imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
            .into(holder.thumbnail)

        // Add exercises dynamically to the LinearLayout
        holder.exercisesContainer.removeAllViews()
        workout.exercises.forEach { exerciseName ->
            val exerciseItemView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.exercise_item, null)
            exerciseItemView.findViewById<TextView>(R.id.exerciseNameTextView).text = exerciseName

            // Add the exercise item to the container
            holder.exercisesContainer.addView(exerciseItemView)
        }

        // Set click listener for the entire item view
        holder.itemView.setOnClickListener {
            listener?.onItemClick(workout)
        }
    }

    // Returns the total number of items in the data set held by the adapter.
    override fun getItemCount(): Int {
        return workoutList.size
    }

    // Function to update the workout list
    fun updateWorkoutList(newWorkoutList: List<Workout>) {
        workoutList = newWorkoutList
        notifyDataSetChanged()
    }
}
