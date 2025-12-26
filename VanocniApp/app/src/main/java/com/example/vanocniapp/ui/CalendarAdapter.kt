package com.example.vanocniapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vanocniapp.databinding.ItemCalendarDayBinding

class CalendarAdapter(private val onDayClick: (Int) -> Unit) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val days = (1..24).toList()
    private var openedDays: Set<Int> = emptySet()

    // Funkce pro aktualizaci seznamu otevřených dnů
    fun setOpenedDays(newOpenedDays: Set<Int>) {
        openedDays = newOpenedDays
        notifyDataSetChanged() // Překreslí celou mřížku
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        val isOpened = openedDays.contains(day)
        holder.bind(day, isOpened)
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size

    class DayViewHolder(private val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Int, isOpened: Boolean) {
            binding.dayTextView.text = day.toString()
            // Pokud je okénko otevřené, uděláme ho lehce průhledným
            binding.root.alpha = if (isOpened) 0.5f else 1.0f
        }
    }
}
