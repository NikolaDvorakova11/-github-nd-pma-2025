package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.myapp007bfragmentsexample1.DetailFragment
import com.example.myapp007bfragmentsexample1.ListFragment
import com.example.myapp007bfragmentsexample1.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Přidáme ListFragment při prvním spuštění
        if (savedInstanceState == null) {
            val listFragment = ListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, listFragment)
                .commit()
        }
    }

    // Voláno při výběru dopravní značky
    fun onSignSelected(name: String, description: String, imageResId: Int) {
        val detailFragment = DetailFragment()

        val bundle = Bundle().apply {
            putString("name", name)
            putString("description", description)
            putInt("imageResId", imageResId)
        }
        detailFragment.arguments = bundle

        // Nahradíme starý fragment novým
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_detail, detailFragment)
            .commit()
    }
}