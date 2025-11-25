package com.example.myapp013mathquizgame

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013mathquizgame.data.DatabaseInstance
import com.example.myapp013mathquizgame.data.Result
import com.example.myapp013mathquizgame.databinding.ActivityGameBinding
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var score = 0
    private var correctAnswer = 0
    private var currentQuestion = 1
    private var playerId: Int = -1

    private var elapsedTime = 0
    private var timerRunning = false

    private val totalQuestions = 5   // počet příkladů

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerId = intent.getIntExtra("player_id", -1)

        if (playerId == -1) {
            Toast.makeText(this, "Chyba: Hráč nenalezen.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.btnSubmit.setOnClickListener {
            checkAnswer()
        }

        startGame()
    }

    private fun startGame() {
        score = 0
        currentQuestion = 1
        elapsedTime = 0
        timerRunning = true

        binding.tvScore.text = "Skóre: 0"
        binding.tvProgress.text = "Příklad: 1 / $totalQuestions"

        startStopwatch()
        generateQuestion()
    }

    private fun startStopwatch() {
        binding.tvTimer.post(object : Runnable {
            override fun run() {
                if (timerRunning) {
                    elapsedTime++
                    binding.tvTimer.text = "Čas: ${elapsedTime}s"
                    binding.tvTimer.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun generateQuestion() {
        val a = Random.nextInt(1, 20)
        val b = Random.nextInt(1, 20)

        correctAnswer = a + b
        binding.tvQuestion.text = "$a + $b = ?"
        binding.etAnswer.text.clear()
    }

    private fun checkAnswer() {
        val userInput = binding.etAnswer.text.toString().toIntOrNull()

        if (userInput == null) {
            Toast.makeText(this, "Zadejte číslo.", Toast.LENGTH_SHORT).show()
            return
        }

        if (userInput == correctAnswer) {
            score++
            binding.tvScore.text = "Skóre: $score"
        } else {
            Toast.makeText(this, "Špatná odpověď!", Toast.LENGTH_SHORT).show()
        }

        if (currentQuestion == totalQuestions) {
            endGame()
            return
        }

        currentQuestion++
        binding.tvProgress.text = "Příklad: $currentQuestion / $totalQuestions"
        generateQuestion()
    }

    private fun endGame() {
        timerRunning = false

        binding.btnSubmit.isEnabled = false
        binding.etAnswer.isEnabled = false

        saveResult()
        showResultDialog()
    }

    private fun showResultDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Výsledek hry")
            .setMessage("Správně: $score / $totalQuestions\nČas: ${elapsedTime}s")
            .setPositiveButton("OK") { _, _ ->
                finish()  // ukončí aktivitu a vrátí se do menu
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }

    private fun saveResult() {
        lifecycleScope.launch {
            val resultDao = DatabaseInstance.getDatabase(applicationContext).resultDao()
            val result = Result(
                playerId = playerId,
                score = score,
                totalQuestions = totalQuestions,
                timeSpent = elapsedTime,
                createdAt = System.currentTimeMillis()
            )
            resultDao.insertResult(result)
        }
    }
}
