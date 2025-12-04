package com.example.myapp014asharedtasklist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp014asharedtasklist.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: TaskAdapter

    //proměnná pro uchování předchozího stavu seznamu
    private var previousTasks: List<Task> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore


        //Úkol 7.1- vytvoření kanálu
        createNotificationChannel()

        //Úkol 7.2 – žádost o povolení (API 33+)
        requestNotificationPermission()


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

    // -------------------------CRUD operace------------------------------
    private fun addTask(title: String) {
        val task = Task(title = title, completed = false)
        db.collection("tasks").add(task)
    }

    private fun toggleCompleted(task: Task) {
        db.collection("tasks")
            .document(task.id)
            .update("completed", !task.completed)
    }

    private fun deleteTask(task: Task) {
        //Upraveno, aby to se objevoval Alert
        AlertDialog.Builder(this)
            .setTitle("Smazat úkol")
            .setMessage("Opravdu chcete tento úkol smazat?")
            .setPositiveButton("Ano") { _, _ ->
                db.collection("tasks")
                    .document(task.id)
                    .delete()
            }
            .setNegativeButton("Ne", null)
            .show()

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


    // -------------------------NOTIFIKACE------------------------------
private fun showNotification(title: String, message: String) {

    // Kontrola povolení pro notifikace (API 33+)
    if (Build.VERSION.SDK_INT >= 33) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return  // povolení není -> notifikaci nezobrazíme
        }
    }

    val builder = NotificationCompat.Builder(this, "tasks_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(this)) {
        notify(System.currentTimeMillis().toInt(), builder.build())
    }
}


    //Úkol 7.1 - Vytvoření Notification Channel
    private fun createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "tasks_channel",
                "Task Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    // 7.2 – Žádost o povolení
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }


    // ---------------------------DETEKCE ZMĚN (7.4)----------------------------
    // Porovná starý a nový seznam úkolů a zjistí, zda některý nebyl nově dokončen
    private fun detectChanges(oldList: List<Task>, newList: List<Task>) {
        newList.forEach { newTask ->
            // Najdeme odpovídající úkol ve starém seznamu podle ID
            val oldTask = oldList.find { it.id == newTask.id }

            // Úkol byl nově dokončen
            if (oldTask != null && !oldTask.completed && newTask.completed) {
                showNotification(
                    "Úkol dokončen",
                    "Úkol „${newTask.title}“ byl označen jako hotový."
                )
            }
        }
    }


    // --------------------------REALTIME LISTENER-----------------------------
    private fun listenForTasks() {
        db.collection("tasks")
            // Sleduje kolekci tasks v reálném čase
            .addSnapshotListener { snapshots, error ->
                // Ošetření chyb
                if (error != null) {
                    Log.w("MainActivity", "Listen failed.", error)
                    return@addSnapshotListener
                }

                // Převede dokumenty z Firestore na seznam objektů Task s přiřazeným ID
                val taskList = snapshots?.map { document ->
                    val task = document.toObject(Task::class.java)
                    task.id = document.id
                    task
                } ?: emptyList()

                // Porovná starý a nový seznam (detekce změn)
                detectChanges(previousTasks, taskList)

                // Uložíme aktuální stav jako "předchozí"
                previousTasks = taskList

                // Aktualizuje RecyclerView novým seznamem úkolů
                adapter.submitList(taskList)
            }
    }
}
