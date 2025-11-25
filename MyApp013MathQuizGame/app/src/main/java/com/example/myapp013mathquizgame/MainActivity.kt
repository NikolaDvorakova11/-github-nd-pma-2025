package com.example.myapp013mathquizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp013mathquizgame.MainMenuActivity
import com.example.myapp013mathquizgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnContinue.setOnClickListener {
            val name = binding.etPlayerName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Zadejte prosím své jméno.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainMenuActivity::class.java)
            intent.putExtra("player_name", name)
            startActivity(intent)
        }
    }
}