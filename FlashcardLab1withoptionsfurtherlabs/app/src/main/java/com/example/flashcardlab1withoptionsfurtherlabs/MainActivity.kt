package com.example.flashcardlab1withoptionsfurtherlabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {

    lateinit var flashcardDatabase: FlashcardDatabase //week 5 lab
    var allFlashcards = mutableListOf<Flashcard>() //week 5 lab
    var currentCardDisplayedIndex = 0 //week 5 lab

    var countDownTimer: CountDownTimer? = null //week 7 lab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        flashcardDatabase = FlashcardDatabase(this) //week 5 lab
        allFlashcards = flashcardDatabase.getAllCards().toMutableList() //week 5 lab

        if(allFlashcards.size > 0) { //week 5 lab 'if' statement

            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }

        flashcardQuestion.setOnClickListener {
            val cx = flashcardAnswer.width/2
            val cy = flashcardAnswer.height/2
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, cx, cy, 0f, finalRadius)
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
            anim.duration = 1000
            anim.start()
        }

        flashcardAnswer.setOnClickListener {
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }

        findViewById<View>(R.id.next_button).setOnClickListener {
            if(allFlashcards.size == 0){
                return@setOnClickListener
            }
            currentCardDisplayedIndex++
            //Lab week 7
            val leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in)
            flashcardQuestion.startAnimation(leftOutAnim)
            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts

                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                    flashcardQuestion.startAnimation(rightInAnim)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })


            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    flashcardQuestion,
                    "You've reached the end of your flashcards - going back to start",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
                currentCardDisplayedIndex = 0
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            val(question, answer) = allFlashcards[currentCardDisplayedIndex]
            flashcardAnswer.text = answer
            flashcardQuestion.text = question
        }


            val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // This code is executed in StartingActivity after we come back from EndingActivity

                // This extracts any data that was passed back from EndingActivity
                val data: Intent? = result.data
                val extras = data?.extras //week 5 lab
                if (extras != null) { //modified by week 5 lab
                    val question = extras.getString("question") //modified by week 5 lab
                    val answer = extras.getString("answer") //modified by week 5 lab
                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer
                    if (question != null && answer != null) {
                        flashcardDatabase.insertCard(Flashcard(question, answer)) //week 5 lab
                        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                    } else {
                        Log.e("TAG", "Missing question or answer to input into database")
                    }
                } else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }
                Snackbar.make(findViewById(R.id.flashcard_question),
                    "Voila! Your question is displayed",
                    Snackbar.LENGTH_SHORT)
                    .show()
            }

            findViewById<View>(R.id.plus_sign_button).setOnClickListener {
                val intent = Intent(this, imageButton::class.java)
                // Launch EndingActivity with the resultLauncher so we can execute more code
                // once we come back here from EndingActivity

                resultLauncher.launch(intent)
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }

    }
}

