package com.example.vanocniapp.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vanocniapp.databinding.ItemCalendarDayBinding
import com.example.vanocniapp.utils.PuzzleCutter

class CalendarAdapter(
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val days = (1..24).toList()
    private var openedDays: Set<Int> = emptySet()
    private var puzzlePieces: List<Bitmap> = emptyList()

    /**
     * Nastaví obrázek skládačky a vygeneruje z něj jednotlivé dílky.
     */
    fun setPuzzleImage(puzzleBitmap: Bitmap) {
        // Obrázek rozřežeme na mřížku 6x4 (24 dílků)
        puzzlePieces = PuzzleCutter.split(puzzleBitmap, 6, 4)
        notifyDataSetChanged()
    }

    /**
     * Aktualizuje seznam otevřených dnů a překreslí mřížku.
     */
    fun setOpenedDays(newOpenedDays: Set<Int>) {
        openedDays = newOpenedDays
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        val isOpened = openedDays.contains(day)
        val piece = puzzlePieces.getOrNull(position)

        holder.bind(day, isOpened, piece)
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size

    class DayViewHolder(private val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Int, isOpened: Boolean, piece: Bitmap?) {
            binding.dayTextView.text = day.toString()

            if (isOpened && piece != null) {
                // Pokud je otevřeno, zobrazíme dílek skládačky a skryjeme číslo
                binding.puzzleImageView.setImageBitmap(piece)
                binding.puzzleImageView.visibility = View.VISIBLE
                binding.dayTextView.visibility = View.GONE
            } else {
                // Jinak zobrazíme jen číslo a skryjeme obrázek
                binding.puzzleImageView.visibility = View.INVISIBLE
                binding.dayTextView.visibility = View.VISIBLE
            }
        }
    }
}
