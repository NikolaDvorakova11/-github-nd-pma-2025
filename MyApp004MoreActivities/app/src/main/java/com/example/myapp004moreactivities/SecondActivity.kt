package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding



class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       //Inicializace View Bindingu
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val startNumber = intent.getStringExtra("START_NUMBER")
        val age = intent.getStringExtra("AGE")

        //Zobrazení přijatých dat
        binding.twInfo.text = "Data z první aktivity. \nPřezdívka: $nickname \nStartovní čílo: $startNumber \nVěk: $age"

        //Tlačítko pro návrat
        binding.btnClose.setOnClickListener {
            finish()
        }

        //Tlačítko pro přechod na třetí aktivitu
        binding.btnThirdAct.setOnClickListener {
           val intent = Intent(this, ThirdActivity::class.java)
           val position = binding.etPosition.text.toString()
           val runTime = binding.etRunTime.text.toString()

            //přidání nových dat
            intent.putExtra("NICK_NAME", nickname)
            intent.putExtra("START_NUMBER", startNumber)
            intent.putExtra("AGE",age)
            intent.putExtra("POSITION",position)
            intent.putExtra("RUN_TIME",runTime)

            startActivity(intent)
        }
    }
}