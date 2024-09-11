package com.example.cravequest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cravequest.R
import com.example.cravequest.models.AuthResponse
import com.example.cravequest.models.SignUpRequest
import com.example.cravequest.network.RetrofitClient
import com.example.cravequest.network.AuthApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var signUpButton: Button
    private lateinit var profileImageView: ImageView
    private lateinit var loginButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        loginButton = findViewById(R.id.login_now)
        loginButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        roleSpinner = findViewById(R.id.role_spinner)
        signUpButton = findViewById(R.id.btnSignUp)
        profileImageView = findViewById(R.id.profile_image)

        // Populate the role spinner with Admin and Customer
        val roles = arrayOf("Admin", "Customer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapter

        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val selectedRole = roleSpinner.selectedItem.toString()

            if (validateInput(username, email, password)) {
                val signUpRequest = SignUpRequest(
                    imageUser = "", // Initially empty, can be set if you allow image upload
                    username = username,
                    email = email,
                    role = selectedRole,
                    password = password
                )

                // Set a placeholder image if no image is provided
                profileImageView.setImageResource(R.drawable.placeholder)

                signUpUser(signUpRequest)
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        )
        return emailPattern.matcher(email).matches()
    }

    private fun signUpUser(request: SignUpRequest) {
        val authApiService = RetrofitClient.instance.create(AuthApiService::class.java)
        val call = authApiService.signUp(request)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    Log.i("SignUp", "Sign up successful, JWT: ${authResponse?.jwt}")
                    // Handle successful sign-up (e.g., navigate to login or home screen)
                } else {
                    Log.e("SignUp", "Sign up failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("SignUp", "Sign up request failed", t)
            }
        })
    }
}
