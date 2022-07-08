package com.zoel.finalmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextName: TextView
    private lateinit var editTextEmailAddress: TextView
    private lateinit var editTextPassword: TextView
    private lateinit var registerButton: Button
    private lateinit var textViewLogin: TextView
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        editTextName = findViewById(R.id.editTextRegisterName)
        editTextEmailAddress = findViewById(R.id.editTextRegisterEmail)
        editTextPassword = findViewById(R.id.editTextRegisterPassword)
        registerButton = findViewById(R.id.registerButton)
        textViewLogin = findViewById(R.id.textViewLogin)
        auth = FirebaseAuth.getInstance()

        //register button
        registerButton.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmailAddress.text.toString()
            val password = editTextPassword.text.toString()

            register(name, email, password) //register method
        }

        //redirect to login
        textViewLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    //logic for register
    private fun register(name: String, email: String, password: String){
        if(name != "" && email != "" && password != ""){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    addUserToDatabase(name, email, auth.currentUser?.uid!!) //method for add user to the database
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@RegisterActivity, "Berhasil registrasi akun.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener { exception ->
                editTextEmailAddress.text = ""
                editTextPassword.text = ""
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        dbRef = FirebaseDatabase.getInstance("https://chat-app-a33a6-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()

        dbRef.child("user").child(uid).setValue(UserModel(name, email, uid))
    }
}