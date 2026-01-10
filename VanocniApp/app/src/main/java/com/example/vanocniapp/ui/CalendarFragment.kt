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
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferencesRepository = UserPreferencesRepository(requireContext())

        setupRecyclerView()
        observeOpenedDays()
        loadPuzzle()
    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarAdapter { day ->
            lifecycleScope.launch {
                // Zjistíme, jestli je nastavené testovací datum
                val mockDate = userPreferencesRepository.mockDateFlow.first()
                val calendar = Calendar.getInstance()
                if (mockDate != null) {
                    calendar.timeInMillis = mockDate
                }

                val today = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)

                // Pokud je prosinec a den je v budoucnosti (podle reálného nebo testovacího data)...
                if (month == Calendar.DECEMBER && day > today) {
                    // ...zobrazíme varovný dialog.
                    showFutureDayConfirmationDialog(day)
                } else {
                    // Jinak den rovnou otevřeme/zavřeme.
                    toggleDayAndNavigate(day)
                }
            }
        }

        binding.calendarRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = calendarAdapter
        }
    }

    private fun showFutureDayConfirmationDialog(day: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Jste si jistí?")
            .setMessage("Opravdu chcete otevřít políčko a zkazit si překvapení?")
            .setPositiveButton("Ano, jsem zvědavý/á") { _, _ -> toggleDayAndNavigate(day) }
            .setNegativeButton("Ne, vydržím to", null)
            .show()
    }

    private fun toggleDayAndNavigate(day: Int) {
        lifecycleScope.launch {
            val isNowOpen = userPreferencesRepository.toggleDayState(day)
            if (isNowOpen) {
                val action = CalendarFragmentDirections.actionCalendarFragmentToDayDetailFragment(day)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeOpenedDays() {
        lifecycleScope.launch {
            userPreferencesRepository.openedDaysFlow.collectLatest {
                calendarAdapter.setOpenedDays(it)
            }
        }
    }

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
