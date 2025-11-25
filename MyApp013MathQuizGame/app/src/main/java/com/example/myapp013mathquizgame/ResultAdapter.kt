package com.example.myapp013mathquizgame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp013mathquizgame.data.Result
import com.example.myapp013mathquizgame.databinding.ItemResultBinding
import java.text.SimpleDateFormat
import java.util.*

class ResultAdapter : ListAdapter<Result, ResultAdapter.ResultViewHolder>(ResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ResultViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {

            binding.tvScore.text = "Skóre: ${result.score}/${result.totalQuestions}"

            val df = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.tvDate.text = "Datum: ${df.format(Date(result.createdAt))}"

            binding.tvTime.text = "Čas: ${result.timeSpent}s"
            binding.tvTime.setTextColor(Color.parseColor("#0066CC"))
        }
    }

    class ResultDiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}