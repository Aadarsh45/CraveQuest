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

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        roleSpinner = findViewById(R.id.role_spinner)
        signUpButton = findViewById(R.id.btnSignUp)
        profileImageView = findViewById(R.id.profile_image)
        loginButton = findViewById(R.id.login_now)

        // Populate the role spinner with Admin and Customer
        val roles = arrayOf("Admin", "Customer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        // Set onClick listener for login button
        loginButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Set onClick listener for sign-up button
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
        return when {
            username.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                false
            }
            !isValidEmail(email) -> {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
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
                    if (authResponse != null) {
                        Log.i("SignUp", "Sign up successful, JWT: ${authResponse.jwt}")
                        Toast.makeText(this@SignUpActivity, "Sign up successful", Toast.LENGTH_SHORT).show()
                        // Handle successful sign-up (e.g., navigate to login or home screen)
                        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                        finish()
                    } else {
                        Log.e("SignUp", "Response body is null")
                        Toast.makeText(this@SignUpActivity, "Sign up failed: Response body is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("SignUp", "Sign up failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@SignUpActivity, "Sign up failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("SignUp", "Sign up request failed", t)
                Toast.makeText(this@SignUpActivity, "Sign up request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
