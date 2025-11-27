package com.example.myapp014asharedtasklist

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp014asharedtasklist.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        // Nastavení adapteru
        adapter = TaskAdapter(
            tasks = emptyList(),
            onChecked = { task -> toggleCompleted(task) },
            onDelete = { task -> deleteTask(task) },
            onEdit = { task -> editTask(task) } //Přidáno pro úkol 2 - editace
        )

        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Přidání úkolu
        binding.buttonAdd.setOnClickListener {
            val title = binding.inputTask.text.toString()
            if (title.isNotEmpty()) {
                addTask(title)
                binding.inputTask.text.clear()
            }
        }

        // Realtime sledování Firestore
        listenForTasks()
    }

    private fun addTask(title: String) {
        println("DEBUG: addTask called with title = $title")
        val task = Task(title = title, completed = false)
        db.collection("tasks").add(task)
    }

    private fun toggleCompleted(task: Task) {
        db.collection("tasks")
            .document(task.id)
            .update("completed", !task.completed)
    }

    private fun deleteTask(task: Task) {
        db.collection("tasks")
            .document(task.id)
            .delete()

    }

    // Úkol 2 - Editace
    private fun editTask(task: Task) {
        val editText = EditText(this)
        editText.setText(task.title)

        AlertDialog.Builder(this)
            .setTitle("Upravit úkol")
            .setView(editText)
            .setPositiveButton("Uložit") { _, _ ->
                val newTitle = editText.text.toString()
                if (newTitle.isNotEmpty()) {
                    db.collection("tasks")
                        .document(task.id)
                        .update("title", newTitle)
                }
            }
            .setNegativeButton("Zrušit", null)
            .show()
    }
    private fun listenForTasks() {
        db.collection("tasks")
            // Sleduje kolekci tasks v reálném čase
            .addSnapshotListener { snapshots, _ ->
                // Úkol /  Převede dokumenty z Firestore na seznam objektů Task
                val taskList = snapshots?.documents?.map { doc ->
                    doc.toObject(Task::class.java)!!.apply { id = doc.id }
                } ?: emptyList()
                // Aktualizuje RecyclerView novým seznamem úkolů
                adapter.submitList(taskList)
            }
    }
}
