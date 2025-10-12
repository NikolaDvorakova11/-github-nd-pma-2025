package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004moreactivities.databinding.ActivityMainBinding
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       //Inicializace View Bindingu
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Hlavní aktivita"

       //Odeslání dat do SecondActivity
        binding.btnSecondAct.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val startNumber = binding.etStartNumber.text.toString()
            val age  = binding.etAge.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("NICK_NAME",nickname)
            intent.putExtra("START_NUMBER", startNumber)
            intent.putExtra("AGE", age)

            startActivity(intent)
            
        }



    }
}