package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val btnSecondAct = findViewById<Button>( R.id.btnSecondAct)
        val etNickname = findViewById<EditText>(R.id.etNickname)
        val etStartNumber = findViewById<EditText>(R.id.etStartNumber)
        val etAge = findViewById<EditText>(R.id.etAge)

        btnSecondAct.setOnClickListener {
            val nickname = etNickname.text.toString()
            val startNumber = etStartNumber.text.toString()
            val age  = etAge.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("NICK_NAME",nickname)
            intent.putExtra("START_NUMBER", startNumber)
            intent.putExtra("AGE", age)

            startActivity(intent)
            
        }



    }
}