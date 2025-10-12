package com.example.myapp004moreactivities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding
import com.example.myapp004moreactivities.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Inicializace View Bindingu
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Souhrn údajů"

        //Přijetí dat
        val nickname = intent.getStringExtra("NICK_NAME")
        val startNumber = intent.getStringExtra("START_NUMBER")
        val age = intent.getStringExtra("AGE")
        val position = intent.getStringExtra("POSITION")
        val runTime = intent.getStringExtra("RUN_TIME")

        //Zobrazení dat v textovém poli
        binding.tvThirdInfo.text = "Údaje běžce: \nPřezdívka: $nickname \nStartovní číslo: $startNumber \nVěk: $age let \nPořadí v závodě: $position \nČas běhu: $runTime"


        //Návrat zpět
        binding.btnThirdBack.setOnClickListener {
            finish()
        }




    }
}