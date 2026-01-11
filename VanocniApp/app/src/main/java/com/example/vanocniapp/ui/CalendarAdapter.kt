package com.example.vanocniapp.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vanocniapp.databinding.ItemCalendarDayBinding
import com.example.vanocniapp.utils.PuzzleCutter
// Adapter se stará o to, aby se v mřížce zobrazilo všech 24 políček.
// onDayClick je funkce, kterou spustíme, když uživatel na políčko klikne.
class CalendarAdapter(
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val days = (1..24).toList() //Vytvoří seznam čísel od 1 do 24 --> Mřížka má 24 polí
    private var openedDays: Set<Int> = emptySet()  //Obsahuje čísla otevřených dnů
    private var puzzlePieces: List<Bitmap> = emptyList() //Obsahuje obrázky jednotlivých dílků skládačky


    /**
     * Nastaví obrázek skládačky a vygeneruje z něj jednotlivé dílky.
     * Tato funkce dostane velký obrázek, rozřeže ho na 24 dílků pomocí "PuzzleCutter"
     */
    fun setPuzzleImage(puzzleBitmap: Bitmap) {
        puzzlePieces = PuzzleCutter.split(puzzleBitmap, 6, 4)
        notifyDataSetChanged()
    }

    /**
     * Aktualizuje seznam otevřených dnů a překreslí mřížku --> aktualizujeme informaci o tom, které dny jsou už otevřené
     */
    fun setOpenedDays(newOpenedDays: Set<Int>) {
        openedDays = newOpenedDays
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    // Funkce, která se volá pro každé políčko v mřížce
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position] //Zjistíme, který je to den (1 až 24)
        val isOpened = openedDays.contains(day) //Zjistíme, zda je daný den již otevřený
        val piece = puzzlePieces.getOrNull(position) // Mámepro něj připravený kousek obrázku ?

        holder.bind(day, isOpened, piece)
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size

    class DayViewHolder(private val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Int, isOpened: Boolean, piece: Bitmap?) {
            binding.dayTextView.text = day.toString()

            // LOGIKA: Pokud je políčko otevřené a máme obrázek...
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
