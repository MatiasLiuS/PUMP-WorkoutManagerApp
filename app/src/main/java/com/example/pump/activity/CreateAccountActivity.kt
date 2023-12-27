package com.example.pump.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pump.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

data class User(val username: String, val birthday: String)

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var confirmPasswordEditText: EditText
    private lateinit var rememberCredentialsSwitch: Switch

    private lateinit var auth: FirebaseAuth
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Find your views by their IDs
        val backButton: Button = findViewById(R.id.backButton)
        val signupButton: Button = findViewById(R.id.signup_button)
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val bdayButton: Button = findViewById(R.id.bdayButton)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)

        rememberCredentialsSwitch = findViewById(R.id.rememberCredentialsSwitch)

        // Set click listeners
        bdayButton.setOnClickListener {
            showDatePickerDialog()
        }

        backButton.setOnClickListener { backButton() }

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()

            if (isValidEmail(email) && password == confirmPassword && password.isNotEmpty() && selectedDate != null && username.isNotEmpty()) {

                if (rememberCredentialsSwitch.isChecked) {
                    saveCredentialsToPreferences(email, password)
                }
                createUser(email, password, username)
            } else {
                showToast("Please enter a valid email, matching passwords, and select a birthday")
            }
        }
    }

    private fun backButton() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun createUser(email: String, password: String, username: String) {
        // Check if the username is already taken
        FirebaseDatabase.getInstance().reference.child("usernames").child(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        // Username is not taken, proceed with user registration
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this@CreateAccountActivity) { task ->
                                if (task.isSuccessful) {
                                    val userId = auth.currentUser?.uid
                                    val user = User(username, selectedDate ?: "")

                                    // Store user information in the Realtime Database
                                    userId?.let {
                                        FirebaseDatabase.getInstance().reference.child("users").child(it)
                                            .setValue(user)
                                    }

                                    // Store the username as a reference to the user ID
                                    FirebaseDatabase.getInstance().reference.child("usernames").child(username)
                                        .setValue(userId)

                                    // Registration successful
                                    showToast("Registration successful")
                                    startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))
                                    finish() // Optional: Close this activity to prevent going back
                                } else {
                                    // Registration failed
                                    showToast("Registration failed: ${task.exception?.message}")
                                }
                            }
                    } else {
                        // Username is already taken
                        showToast("Username is already taken, please choose another one")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    showToast("Username check failed: ${error.message}")
                }
            })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Handle the selected date
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                updateSelectedDateTextView()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun updateSelectedDateTextView() {
        val selectedDateTextView: TextView = findViewById(R.id.selectedDateTextView)
        selectedDateTextView.text = selectedDate
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun saveCredentialsToPreferences(email: String, password: String) {
        val preferences: SharedPreferences =
            getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_password", password)
        editor.apply()
    }

}
