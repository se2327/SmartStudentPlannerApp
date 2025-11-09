package com.example.practiceproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class Feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feedback)
        val r=findViewById<RatingBar>(R.id.rating)
        val submitRatingButton=findViewById<Button>(R.id.ratingButton)
        val feed=findViewById<EditText>(R.id.editfeedback)
        val charcounter=findViewById<TextView>(R.id.CharacterCount)
        val submitFeedback=findViewById<Button>(R.id.FeedbackButton)

        r.setOnRatingBarChangeListener{ _, rating, _ ->
            if(rating>0f){
                submitRatingButton.visibility=Button.VISIBLE
            }
        }

        submitRatingButton.setOnClickListener{
            val i=r.rating
            Snackbar.make(it,"You rated us $i star(s)",Snackbar.LENGTH_SHORT).show()
        }

        feed.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(i: Editable?){
                val l=if(i !=null) i.length else 0
                charcounter.text="$l/ 250"
                    submitFeedback.visibility=if(l>0) Button.VISIBLE else Button.GONE
            }
            override fun beforeTextChanged(p0:CharSequence? ,p1:Int, p2:Int, p3: Int){}
            override fun onTextChanged(p0:CharSequence?, p1:Int, p2:Int, p3:Int){}
        })

        submitFeedback.setOnClickListener{
            val feedbacktext= feed.text.toString()
            val i=feedbacktext.length
            Snackbar.make(it,"You submitted a feedback of $i characters",Snackbar.LENGTH_SHORT).show()
        }

    }
}