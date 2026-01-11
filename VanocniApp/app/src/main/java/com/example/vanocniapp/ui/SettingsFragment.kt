package com.example.vanocniapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vanocniapp.data.UserPreferencesRepository
import com.example.vanocniapp.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferencesRepository: UserPreferencesRepository

    // Formát pro formátování datumu --> převede se počítačové datum na lidsky čitelný formát "1. 12. 2025"
    private val dateFormatter = SimpleDateFormat("d. M. yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferencesRepository = UserPreferencesRepository(requireContext())

        setupClickListeners()  // Nastavíme všechnyOnClickListenery
        observeMockDate()      // Zobrazujeme aktuální testovací datum
    }

    // NASTAVENÍ KLIKNUTÍ
    private fun setupClickListeners() {
        // Kliknutí na kartu pro resetování kalendáře
        binding.resetCalendarCard.setOnClickListener { showResetConfirmationDialog() }
        // Kliknutí na kartu pro nastavení testovacího data
        binding.setDateCard.setOnClickListener { showDatePickerDialog() }
        // Kliknutí na tlačítko pro smazání testovacího data
        binding.clearDateButton.setOnClickListener { clearMockDate() }
    }

    // SLEDOVÁNÍ TESTOVACÍHO DATUMU
    private fun observeMockDate() {
        lifecycleScope.launch {
            // Nasloucháme testovací datum
            userPreferencesRepository.mockDateFlow.collectLatest {
                if (it != null) {
                    // Pokud je nastavené testovací datum, zobrazíme ho v textovém poli
                    val calendar = Calendar.getInstance().apply { timeInMillis = it }
                    binding.currentMockDateTextView.text = "Testovací datum: ${dateFormatter.format(calendar.time)}"
                    binding.clearDateButton.visibility = View.VISIBLE   // Ukážeme tlačítko pro smazání
                } else {
                    // Jinak napíšeme, že se používá reálný čas z mobilu
                    binding.currentMockDateTextView.text = "Aplikace používá reálné datum."
                    binding.clearDateButton.visibility = View.GONE
                }
            }
        }
    }

    // VÝBĚR DATUMU (Kalendářové okno)
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        // Otevře systémové okno pro výběr dne/měsíce/roku
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Správný a bezpečný způsob, jak nastavit datum bez časových chyb --> zpracujeme, co uživatel vybral
                val selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    // Vynulujeme čas, abychom se vyhnuli problémům s časovými zónami --> nastavíme ho na 00:00:00
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                lifecycleScope.launch {
                    // Uložíme vybraný čas do paměti mobilu
                    userPreferencesRepository.setMockDate(selectedDate.timeInMillis)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun clearMockDate() {
        lifecycleScope.launch {
            userPreferencesRepository.clearMockDate()
            Snackbar.make(binding.root, "Testovací datum bylo zrušeno.", Snackbar.LENGTH_SHORT).show()
        }
    }

    // RESETOVÁNÍ OTEVŘENÝCH POLÍČEK
    private fun showResetConfirmationDialog() {
        // Dialog pro potvrzení resetování kalendáře --> Nejdřív se pro jistotu zeptáme, aby si uživatel nesmazal postup omylem
        AlertDialog.Builder(requireContext())
            .setTitle("Opravdu resetovat?")
            .setMessage("Všechna otevřená políčka budou smazána. Tuto akci nelze vrátit zpět.")
            .setPositiveButton("Ano, resetovat") { _, _ -> resetCalendar() }
            .setNegativeButton("Zrušit", null)
            .show()
    }

    private fun resetCalendar() {
        lifecycleScope.launch {
            userPreferencesRepository.clearOpenedDays()     // Smazání otevřených políček
            //Vytvoříme a zobrazíme Snackbar (malou vyskakovací lištu ve spodní části obrazovky)
            Snackbar.make(binding.root, "Kalendář byl úspěšně resetován.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
