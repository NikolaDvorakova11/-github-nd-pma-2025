package com.example.myapp013mathquizgame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp013mathquizgame.data.DatabaseInstance
import com.example.myapp013mathquizgame.databinding.ActivityResultsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ResultAdapter()

        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        val resultDao = DatabaseInstance.getDatabase(this).resultDao()

        lifecycleScope.launch {
            resultDao.getAllResults().collectLatest { results ->
                if (results.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                }
                adapter.submitList(results)
            }
        }

        binding.btnBackToMenu.setOnClickListener {
            finish()
        }
    }
}