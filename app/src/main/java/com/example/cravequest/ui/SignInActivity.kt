package com.example.cravequest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cravequest.MainActivity
import com.example.cravequest.R
import com.example.cravequest.models.AuthResponse
import com.example.cravequest.models.LoginRequest
import com.example.cravequest.network.RetrofitClient
import com.example.cravequest.network.AuthApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var signInButton: Button
    private lateinit var signUpLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize views
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)

        signInButton = findViewById(R.id.btnSignIn)
        signUpLink = findViewById(R.id.register_now)



        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()


            if (validateInput(email, password)) {
                val loginRequest = LoginRequest(
                    email = email,

                    password = password
                )

                signInUser(loginRequest)
            }
        }

        // Handle Sign Up link click
        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
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

    private fun signInUser(request: LoginRequest) {
        val authApiService = RetrofitClient.instance.create(AuthApiService::class.java)
        val call = authApiService.signIn(request)

        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {

            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    // Save JWT Token to SharedPreferences
                    val sharedPreferences = getSharedPreferences("CraveQuest", MODE_PRIVATE)
                    sharedPreferences.edit().putString("JWT_TOKEN", authResponse.jwt).apply()

                    Toast.makeText(this@SignInActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Move to the next activity after successful login
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
            } else {
                Toast.makeText(this@SignInActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }

            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("SignIn", "Sign in request failed", t)
            }
        })
    }
}
