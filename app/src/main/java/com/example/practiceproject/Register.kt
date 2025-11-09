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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        FirebaseApp.initializeApp(this)
        val a=FirebaseAuth.getInstance()
        val email=findViewById<EditText>(R.id.RegisterEditEmailAddress)
        val password=findViewById<EditText>(R.id.RegisterEditPassword)
        val Registerbtn=findViewById<Button>(R.id.RegisterButton)
        val loginbtn=findViewById<TextView>(R.id.Register_LoginButontext)

        val text = "Already have an account? <font color='#FF9800'><b>Login</b></font>"
        loginbtn.text = android.text.Html.fromHtml(text)

        Registerbtn.setOnClickListener{
            //which function creates the entry in the databasefor email password--- createUserWithEmailAndPassword
            val emailText=email.text.toString()
            val passwordText=password.text.toString()
            if(emailText.isNotEmpty()&& passwordText.isNotEmpty()){
                a.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener{ task ->

                    if(task.isSuccessful){
                        Toast.makeText(this,"Registration Success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))

                    }else{
                        Toast.makeText(this,"Registration Failed ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }

        }
        loginbtn.setOnClickListener{
            startActivity(Intent(this,LoginIn::class.java))
            finish()
        }

    }
}