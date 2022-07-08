package com.zoel.finalmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextLoginEmail: EditText
    private lateinit var editTextLoginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var textViewRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        editTextLoginEmail = findViewById(R.id.editTextLoginEmail)
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword)
        loginButton = findViewById(R.id.loginButton)
        textViewRegister = findViewById(R.id.textViewRegister)
        auth = FirebaseAuth.getInstance()

        //login button
        loginButton.setOnClickListener{
            val email = editTextLoginEmail.text.toString()
            val password = editTextLoginPassword.text.toString()
            login(email, password) //login method
        }

        //go to register
        textViewRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    //logic for login auth
    private fun login(email: String, password: String){
        if(email != "" && password != "") {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener { exception ->
                editTextLoginEmail.setText("")
                editTextLoginPassword.setText("")
                Toast.makeText(this@LoginActivity, "Email atau password salah!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}