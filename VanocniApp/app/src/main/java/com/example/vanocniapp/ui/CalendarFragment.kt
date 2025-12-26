package com.example.vanocniapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vanocniapp.data.UserPreferencesRepository
import com.example.vanocniapp.databinding.FragmentCalendarBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    }

    private fun setupRecyclerView() {
        calendarAdapter = CalendarAdapter { day ->
            // Uložíme otevřený den a pak přejdeme na detail
            lifecycleScope.launch {
                userPreferencesRepository.addOpenedDay(day)
            }
            val action = CalendarFragmentDirections.actionCalendarFragmentToDayDetailFragment(day)
            findNavController().navigate(action)
        }

        binding.calendarRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = calendarAdapter
        }
    }

    private fun observeOpenedDays() {
        lifecycleScope.launch {
            // Sledujeme Flow a při každé změně aktualizujeme adapter
            userPreferencesRepository.openedDaysFlow.collectLatest {
                calendarAdapter.setOpenedDays(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
