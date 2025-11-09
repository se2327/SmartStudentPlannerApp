package com.example.practiceproject

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val l=findViewById<ConstraintLayout>(R.id.splash)
        val fade=AnimationUtils.loadAnimation(this,R.anim.fade_in)
        l.startAnimation(fade)
        
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,Register::class.java))
            finish()
        },5000)


    }
}