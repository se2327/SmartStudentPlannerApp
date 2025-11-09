package com.example.practiceproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_in)
        val a= FirebaseAuth.getInstance()
        val emailEditText=findViewById<EditText>(R.id.LoginEditEmail)
        val passwordEditText=findViewById<EditText>(R.id.LoginEditPassword)
        val loginBtn=findViewById<Button>(R.id.Loginbutton)
        val RegisterRedirect=findViewById<TextView>(R.id.Login_RegisterBuOtontext)

        val text = "Don't have an account? <font color='#FF9800'><b>Register</b></font>"
        RegisterRedirect.text = android.text.Html.fromHtml(text)

        loginBtn.setOnClickListener{
            val email=emailEditText.text.toString().trim()
            val password=passwordEditText.text.toString().trim()

            if(email.isNotEmpty()&&password.isNotEmpty()){
                a.signInWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Login failed:${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"this,Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        RegisterRedirect.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
            finish()
        }
    }
}
