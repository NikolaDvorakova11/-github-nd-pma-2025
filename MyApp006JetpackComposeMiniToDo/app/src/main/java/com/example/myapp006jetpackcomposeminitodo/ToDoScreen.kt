package com.example.myapp006jetpackcomposeminitodo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

data class TodoItem(
    val title: String,
    val isDone: Boolean = false
)

@Composable
fun ToDoScreen(modifier: Modifier = Modifier) {
    /*Text("TO-DO SCREEN",
        modifier = modifier
    )*/

    // Stav pro textové pole
    var text by remember { mutableStateOf("") }

    // Stav seznamu úkolů
    val tasks = remember { mutableStateListOf<TodoItem>() }

    // Úkol – stav pro dialog smazání
    var taskToDelete by remember { mutableStateOf<TodoItem?>(null) }
    // Úkol - dialog pro smazání všech splněných úkolů
    var showDeleteCompletedDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        // ----- Řádek s TextField + tlačítkem -----
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nový úkol") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        tasks.add(TodoItem(title = text))
                        text = ""  // vymazat pole
                    }
                }
            ) {
                Text("+")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        // ÚKOL - status úkolů
        val total = tasks.size
        val done = tasks.count { it.isDone }

        Text(
            text = "$total úkolů celkem, $done splněno",
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        //Úkol - vložení tlačítka pro smazání všech hotových úkolů
        Button(
            onClick = { showDeleteCompletedDialog = true },
            enabled = tasks.any { it.isDone },   // aktivní jen když existují hotové úkoly
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Vymazat všechny hotové úkoly")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----- Seznam úkolů -----
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val index = tasks.indexOf(task)
                            tasks[index] = task.copy(isDone = !task.isDone)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (task.isDone) Color(0xFFA1E165) else Color.White   // ÚKOL - změna barvy pole v případě, že je přeškrtnuta
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = task.title,
                            textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                        )

                        // ÚKOL – místo TextButton používáme IconButton
                        IconButton(
                            onClick = { taskToDelete = task }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_delete_24),
                                contentDescription = "Smazat úkol"
                            )
                        }
                    }
                }
            }
        }
        // ÚKOL – potvrzovací dialog o smazání
        if (taskToDelete != null) {
            AlertDialog(
                onDismissRequest = { taskToDelete = null },
                title = { Text("Potvrzení smazání") },
                text = { Text("Opravdu chcete smazat tuto poznámku?") },
                confirmButton = {
                    TextButton(onClick = {
                        tasks.remove(taskToDelete)
                        taskToDelete = null
                    }) {
                        Text("Ano")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { taskToDelete = null }) {
                        Text("Ne")
                    }
                }
            )
        }
    }

    // ÚKOL – dialog smazat všechny hotové úkoly
    if (showDeleteCompletedDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteCompletedDialog = false },
            title = { Text("Smazat hotové úkoly") },
            text = { Text("Opravdu chcete smazat všechny dokončené úkoly?") },
            confirmButton = {
                TextButton(onClick = {
                    tasks.removeAll { it.isDone }
                    showDeleteCompletedDialog = false
                }) {
                    Text("Smazat", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteCompletedDialog = false }) {
                    Text("Zrušit")
                }
            }
        )
    }
}
