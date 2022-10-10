package com.example.flashcardlab1withoptionsfurtherlabs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class imageButton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_button)


        findViewById<View>(R.id.cancelButton).setOnClickListener {
            val finished = Intent(this, MainActivity::class.java)
            startActivity(finished)

        }

        findViewById<View>(R.id.saveButton).setOnClickListener {
            Intent(this, MainActivity::class.java)

            val data = Intent()

            data.putExtra(
                "question",
                findViewById<EditText>(R.id.questionTextField).text.toString()
            )

            data.putExtra(
                "answer",
                findViewById<EditText>(R.id.answerTextField).text.toString()
            )

            setResult(RESULT_OK, data)

            finish()

        }

    }

}
