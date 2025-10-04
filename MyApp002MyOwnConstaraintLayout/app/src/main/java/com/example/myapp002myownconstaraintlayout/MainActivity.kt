package com.example.myapp002myownconstaraintlayout

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            return@setOnApplyWindowInsetsListener insets}

            val etAge = findViewById<EditText>(R.id.etAge)
            val etHeight = findViewById<EditText>(R.id.etHeight)
            val etWeight = findViewById<EditText>(R.id.etWeight)
            val rgGender = findViewById<RadioGroup>(R.id.rgGender)
            val btnCalculate = findViewById<Button>(R.id.btnCalculate)
            val btnReset = findViewById<Button>(R.id.btnReset)
            val tvResult = findViewById<TextView>(R.id.tvResult)

            btnCalculate.setOnClickListener {
                val ageStr = etAge.text.toString().trim()
                val heightStr = etHeight.text.toString().trim().replace(',', '.')
                val weightStr = etWeight.text.toString().trim().replace(',', '.')

                if (ageStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
                    tvResult.text = "Prosím zadejte věk, výšku i hmotnost."
                    return@setOnClickListener
                }

                val age = ageStr.toIntOrNull()
                val heightCm = heightStr.toFloatOrNull()
                val weightKg = weightStr.toFloatOrNull()

                if (age == null || age !in 5..120) {
                    tvResult.text = "Zadejte věk v rozmezí 5–120 let."
                    return@setOnClickListener
                }
                if (heightCm == null || heightCm < 50f || heightCm > 250f) {
                    tvResult.text = "Zadejte reálnou výšku (50–250 cm)."
                    return@setOnClickListener
                }
                if (weightKg == null || weightKg < 10f || weightKg > 400f) {
                    tvResult.text = "Zadejte reálnou hmotnost (10–400 kg)."
                    return@setOnClickListener
                }

                val heightM = heightCm / 100
                val bmi = weightKg / heightM.pow(2)

                val category = when {
                    bmi < 18.5 -> "Podváha"
                    bmi < 24.9 -> "Normální váha"
                    bmi < 29.9 -> "Nadváha"
                    else -> "Obezita"
                }

                val gender = when (rgGender.checkedRadioButtonId) {
                    R.id.rbMale -> "Muž"
                    R.id.rbFemale -> "Žena"
                    else -> "Neuvedeno"
                }

                val note = if (age < 20) "\nPoznámka: U osob mladších 20 let je BMI pouze orientační." else ""

                tvResult.setBackgroundResource(R.drawable.bg_result) // zobrazí šedé pozadí
                tvResult.text = "BMI: %.2f\nKategorie: %s\nVěk: %d let\nPohlaví: %s%s".format(
                    bmi, category, age, gender, note
                )
                // RESET FORMULÁŘE
                btnReset.setOnClickListener {
                    etAge.text.clear()
                    etHeight.text.clear()
                    etWeight.text.clear()
                    rgGender.clearCheck()
                    tvResult.text = "Zadejte údaje a stiskněte tlačítko."
                    tvResult.background = null
                }
            }
        }
    }

