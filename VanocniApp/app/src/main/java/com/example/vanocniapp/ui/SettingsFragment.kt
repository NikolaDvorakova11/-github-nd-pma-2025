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

        setupClickListeners()
        observeMockDate()
    }

    private fun setupClickListeners() {
        binding.resetCalendarCard.setOnClickListener { showResetConfirmationDialog() }
        binding.setDateCard.setOnClickListener { showDatePickerDialog() }
        binding.clearDateButton.setOnClickListener { clearMockDate() }
    }

    /** Sleduje a zobrazuje aktuální testovací datum. */
    private fun observeMockDate() {
        lifecycleScope.launch {
            userPreferencesRepository.mockDateFlow.collectLatest {
                if (it != null) {
                    val calendar = Calendar.getInstance().apply { timeInMillis = it }
                    binding.currentMockDateTextView.text = "Testovací datum: ${dateFormatter.format(calendar.time)}"
                    binding.clearDateButton.visibility = View.VISIBLE
                } else {
                    binding.currentMockDateTextView.text = "Aplikace používá reálné datum."
                    binding.clearDateButton.visibility = View.GONE
                }
            }
        }
    }

    /** Zobrazí dialog pro výběr data. */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
                lifecycleScope.launch {
                    userPreferencesRepository.setMockDate(selectedDate.timeInMillis)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /** Smaže nastavené testovací datum. */
    private fun clearMockDate() {
        lifecycleScope.launch {
            userPreferencesRepository.clearMockDate()
            Snackbar.make(binding.root, "Testovací datum bylo zrušeno.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Opravdu resetovat?")
            .setMessage("Všechna otevřená políčka budou smazána. Tuto akci nelze vrátit zpět.")
            .setPositiveButton("Ano, resetovat") { _, _ -> resetCalendar() }
            .setNegativeButton("Zrušit", null)
            .show()
    }

    private fun resetCalendar() {
        lifecycleScope.launch {
            userPreferencesRepository.clearOpenedDays()
            Snackbar.make(binding.root, "Kalendář byl úspěšně resetován.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
