package com.example.flashcardlab1withoptionsfurtherlabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }

        flashcardAnswer.setOnClickListener {
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }


            val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // This code is executed in StartingActivity after we come back from EndingActivity

                // This extracts any data that was passed back from EndingActivity
                val data: Intent? = result.data
                if (data != null) {
                    val string1 = data.getStringExtra("string1")
                    val string2 = data.getStringExtra("string2")
                    flashcardQuestion.text = string1
                    flashcardAnswer.text =string2
                } else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }

                Snackbar.make(findViewById(R.id.flashcard_question),
                    "The message to display",
                    Snackbar.LENGTH_SHORT)
                    .show()

            }

            findViewById<View>(R.id.plus_sign_button).setOnClickListener {
                val intent = Intent(this, imageButton::class.java)
                // Launch EndingActivity with the resultLauncher so we can execute more code
                // once we come back here from EndingActivity

                resultLauncher.launch(intent)

            }

    }
}

