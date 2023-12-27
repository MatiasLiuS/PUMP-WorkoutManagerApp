package com.example.pump.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pump.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Firebase authentication
    private lateinit var auth: FirebaseAuth
    private lateinit var saveCredentialsSwitch: Switch

    // UI elements
    private lateinit var emailOrUsernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // Find UI elements by their IDs
        emailOrUsernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginbutton)
        registerButton = findViewById(R.id.registerbutton)
        // Set up click listeners for buttons

        saveCredentialsSwitch = findViewById(R.id.saveCredentialsSwitch)
        saveCredentialsSwitch.isChecked = true

        // Set up click listeners for buttons

        preFillCredentialsFromPreferences()

        setupListeners()
    }

    // Function to set up click listeners for login and register buttons
    private fun setupListeners() {
        loginButton.setOnClickListener {
            // Retrieve email/username and password from the input fields
            val emailOrUsername = emailOrUsernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Attempt to log in the user
            loginUser(emailOrUsername, password)
            // Save credentials if the switch is turned on
            if (saveCredentialsSwitch.isChecked) {
                saveCredentialsToPreferences(emailOrUsername, password)
            } else {

                clearSavedCredentials()
                // Clear the EditText fields using a Handler to ensure UI updates
            }
        }


        registerButton.setOnClickListener {
            // Navigate to the CreateAccountActivity
            loadCreateAccountActivity()
        }


    }



    // Function to authenticate the user based on email or username
    private fun loginUser(emailOrUsername: String, password: String) {
        // Check if the input is a valid email address
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailOrUsername).matches()

        if (isEmail) {
            // If it's a valid email address, use it for authentication
            auth.signInWithEmailAndPassword(emailOrUsername, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login success, navigate to the UserActivity
                        val intent = Intent(this, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign-in fails, display an error message to the user
                        val errorMessage = task.exception?.message ?: "Authentication failed."
                        showToast(errorMessage)
                    }
                }
        } else {
            showToast("Username-based authentication not implemented.")
        }
    }

    // Function to navigate to the CreateAccountActivity
    private fun loadCreateAccountActivity() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    // Function to display a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Pre-fill EditText fields based on saved preferences
    private fun preFillCredentialsFromPreferences() {
        val preferences: SharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val rememberedEmail = preferences.getString("user_email", "")
        val rememberedPassword = preferences.getString("user_password", "")

        // Set the pre-filled values to the respective EditText fields
        emailOrUsernameEditText.setText(rememberedEmail)
        passwordEditText.setText(rememberedPassword)
    }
    // Clear saved credentials from preferences
    private fun clearSavedCredentials() {
        val preferences: SharedPreferences =
            getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
    // Add this method to save credentials to preferences
    private fun saveCredentialsToPreferences(email: String, password: String) {
        val preferences: SharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()

        // Save the email and password to preferences
        editor.putString("user_email", email)
        editor.putString("user_password", password)

        // Apply the changes
        editor.apply()
    }

}
