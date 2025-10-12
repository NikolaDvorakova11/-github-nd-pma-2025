package com.example.myapp004moreactivities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)

        val twInfo = findViewById<TextView>(R.id.twInfo)

        //Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val startNumber = intent.getStringExtra("START_NUMBER")
        val age = intent.getStringExtra("AGE")

        //Zobrazení přijatých dat
        twInfo.text = "Data z první aktivity. \nPřezdívka: $nickname \nStartovní čílo: $startNumber \nVěk: $age"

        val btnClose = findViewById<Button>(R.id.btnClose)

        //Tlačítko pro návrat
        btnClose.setOnClickListener {
            finish()
        }
    }
}