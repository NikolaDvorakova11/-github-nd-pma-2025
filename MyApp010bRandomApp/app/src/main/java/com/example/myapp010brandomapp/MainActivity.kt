package com.example.myapp010brandomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.myapp010brandomapp.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aktivace ViewBindingu
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Přepínač pro dvě kostky – zapíná a vypíná druhou kostku + text součtu
        binding.switchTwoDice.setOnCheckedChangeListener { _, isChecked ->
            binding.ivDice2.visibility =
                if (isChecked) ImageView.VISIBLE else ImageView.GONE

            binding.tvSum.visibility =
                if (isChecked) View.VISIBLE else View.GONE
        }

        // Tlačítko hodu
        binding.btnRoll.setOnClickListener {
            rollDice()
        }
    }

    /**
     * Provede animaci kostky a nastaví náhodné hodnoty
     */
    private fun rollDice() {

        // Animace rotace
        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)

        // První kostka
        val num1 = Random.nextInt(1, 7)
        binding.ivDice1.startAnimation(rotate)
        binding.ivDice1.setImageResource(getDiceImage(num1))

        // Druhá kostka – pouze pokud je aktivní režim dvou kostek
        if (binding.switchTwoDice.isChecked) {
            val num2 = Random.nextInt(1, 7)
            binding.ivDice2.startAnimation(rotate)
            binding.ivDice2.setImageResource(getDiceImage(num2))

            // vypočítání součtu
            val sum = num1 + num2
            binding.tvSum.text = "Součet: $sum"
        } else {
            // pokud je jen jedna kostka → součet se nesmí zobrazit
            binding.tvSum.visibility = View.GONE
        }
    }

    /**
     * Vrací obrázek podle čísla hodu
     */
    private fun getDiceImage(number: Int): Int {
        return when (number) {
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            else -> R.drawable.dice6
        }
    }
}
