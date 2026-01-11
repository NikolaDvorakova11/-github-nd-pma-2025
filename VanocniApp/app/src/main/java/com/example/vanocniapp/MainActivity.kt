package com.example.vanocniapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vanocniapp.databinding.ActivityMainBinding

// MainActivity je hlavní aktivitou aplikace, která obsahuje navigaci mezi fragmenty
class MainActivity : AppCompatActivity() {
    // Deklarace proměnných (Teď tu proměnnou vytvořím, ale hodnotu do ní vložím až za chvíli (v onCreate))
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Deklarace metody onCreate, která se volá při vytvoření aktivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializace ViewBindingu
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Najdeme NavController pro navigaci mezi fragmenty
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 2. Nastavíme Toolbar jako hlavní ActionBar
        setSupportActionBar(binding.toolbar)

        // 3. Vytvoříme AppBarConfiguration, která propojí navigaci s DrawerLayout
        appBarConfiguration = AppBarConfiguration(
            // Zde definujeme hlavní obrazovky, na kterých se bude zobrazovat "hamburger" ikona
            setOf(R.id.calendarFragment, R.id.settingsFragment, R.id.aboutFragment),
            binding.drawerLayout
        )

        // 4. Propojíme ActionBar s NavControllerem
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 5. Propojíme spodní i boční menu s NavControllerem
        binding.bottomNav.setupWithNavController(navController)
        binding.drawerNav.setupWithNavController(navController)
    }

    // Zajišťuje správnou funkci tlačítka "zpět" a "hamburger" ikony v horní liště
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
