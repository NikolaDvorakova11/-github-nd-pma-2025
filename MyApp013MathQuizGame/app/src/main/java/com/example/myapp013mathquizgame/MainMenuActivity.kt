package com.example.myapp013mathquizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013mathquizgame.data.DatabaseInstance
import com.example.myapp013mathquizgame.data.Player
import com.example.myapp013mathquizgame.databinding.ActivityMainMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private var activePlayerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerName = intent.getStringExtra("player_name") ?: "Neznámý hráč"
        binding.tvPlayerName.text = "Hráč: $playerName"

        // Zablokujeme tlačítko, dokud není ID načteno
        binding.btnPlay.isEnabled = false

        // Uložit nebo načíst hráče
        handlePlayerIdentity(playerName)

        // Spustit hru
        binding.btnPlay.setOnClickListener {
            if (activePlayerId > 0) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("player_id", activePlayerId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Chyba: ID hráče není načteno.", Toast.LENGTH_LONG).show()
            }
        }

        // Zobrazit výsledky
        binding.btnResults.setOnClickListener {
            startActivity(Intent(this, ResultsActivity::class.java))
        }
    }

    private fun handlePlayerIdentity(playerName: String) {
        val db = DatabaseInstance.getDatabase(applicationContext)
        val playerDao = db.playerDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val existingPlayer = playerDao.getPlayerByName(playerName)

            val finalId = if (existingPlayer == null) {
                val newPlayer = Player(name = playerName)
                playerDao.insertPlayer(newPlayer).toInt()
            } else {
                existingPlayer.id
            }

            activePlayerId = finalId
            saveActivePlayerId(finalId)

            withContext(Dispatchers.Main) {
                binding.btnPlay.isEnabled = true
            }
        }
    }

    private fun saveActivePlayerId(id: Int) {
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("ACTIVE_PLAYER_ID", id)
            apply()
        }
    }
}