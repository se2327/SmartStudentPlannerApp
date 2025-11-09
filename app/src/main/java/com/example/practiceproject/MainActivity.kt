package com.example.practiceproject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)  //by default

        toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, android.R.color.black))// 3 dot menu icon

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        loadFragment(DashboardFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }

                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                R.id.nav_assignments ->{
                    loadFragment(AssignmentFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


    override fun onCreateOptionsMenu(m: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, m)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.rateus -> {
                val intent = Intent(this, Feedback::class.java)
                startActivity(intent)
                true
            }

            R.id.logout -> {
//                Toast.makeText(this,"will add functionality soon",Toast.LENGTH_SHORT).show()

                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Register::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
