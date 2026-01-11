package com.example.vanocniapp.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vanocniapp.R
import com.example.vanocniapp.data.UserPreferencesRepository
import com.example.vanocniapp.databinding.FragmentCalendarBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    // Repository je třída, která se stará o ukládání dat (např. které dny jsou otevřené)
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    // Vytvoříme layout pro fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Zobrazujeme fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vytvoříme repository --> incicializujeme úložiště dat
        userPreferencesRepository = UserPreferencesRepository(requireContext())

        setupRecyclerView()     // Nastavíme recycler view --> mřížku (grid)
        observeOpenedDays()     // Začneme sledovat změny v otevřených dnech (co je otevřeno)
        loadPuzzle()            // Načteme obrázek skládačky
    }

    // Nastavení mřížky 4x6 pro 24 políček
    private fun setupRecyclerView() {
        // Tato část se spustí, když klikneš na políčko (číslo dne)
        calendarAdapter = CalendarAdapter { day ->
            lifecycleScope.launch {
                // Zjistíme "aktuální čas" (buď reálný, nebo ten nastavený v testování)
                val mockDate = userPreferencesRepository.mockDateFlow.first()
                val calendar = Calendar.getInstance()
                if (mockDate != null) {
                    calendar.timeInMillis = mockDate
                }

                val today = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)

                // Pokud je prosinec a den je v budoucnosti (podle reálného nebo testovacího data)...
                if (month == Calendar.DECEMBER && day > today) {
                    // ...zobrazíme varovný dialog
                    showFutureDayConfirmationDialog(day)
                } else {
                    // Jinak den rovnou otevřeme
                    toggleDayAndNavigate(day)
                }
            }
        }

        binding.calendarRecyclerView.apply {
            // GridLayoutManager se stará o to, aby políčka byla v mřížce (4 sloupce)
            layoutManager = GridLayoutManager(context, 4)
            adapter = calendarAdapter
        }
    }

    // Dialog, který vyskočí, když je uživatel nedočkavý
    private fun showFutureDayConfirmationDialog(day: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Jste si jistí?")
            .setMessage("Opravdu chcete otevřít políčko a zkazit si překvapení?")
            .setPositiveButton("Ano, jsem zvědavý/á") { _, _ -> toggleDayAndNavigate(day) }
            .setNegativeButton("Ne, vydržím to", null)
            .show()
    }

    // Tato funkce uloží, že den je otevřený, a přepne nás na detailní obrazovku
    private fun toggleDayAndNavigate(day: Int) {
        lifecycleScope.launch {
            val isNowOpen = userPreferencesRepository.toggleDayState(day)
            if (isNowOpen) {
                // Navigace na obrazovku DayDetailFragment s předáním čísla dne
                val action = CalendarFragmentDirections.actionCalendarFragmentToDayDetailFragment(day)
                findNavController().navigate(action)
            }
        }
    }

    // "Posluchač", který hlídá změny v datech.
    // Jakmile se v databázi změní stav (otevření dne), adapter políčka okamžitě překreslí.
    private fun observeOpenedDays() {
        lifecycleScope.launch {
            userPreferencesRepository.openedDaysFlow.collectLatest {
                calendarAdapter.setOpenedDays(it)
            }
        }
    }

    // Načte velký obrázek z prostředků aplikace (res/drawable/puzzle_image)
    private fun loadPuzzle() {
        try {
            val puzzleBitmap = BitmapFactory.decodeResource(resources, R.drawable.puzzle_image)
            if (puzzleBitmap != null) {
                calendarAdapter.setPuzzleImage(puzzleBitmap)
            } else {
                Log.e("CalendarFragment", "Chyba: Soubor 'puzzle_image.jpg' nebyl nalezen ve složce res/drawable.")
            }
        } catch (e: Exception) {
            Log.e("CalendarFragment", "Došlo k výjimce při načítání obrázku skládačky.", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
