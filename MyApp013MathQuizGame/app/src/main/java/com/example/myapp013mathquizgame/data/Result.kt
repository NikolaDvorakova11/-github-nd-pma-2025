package com.example.myapp013mathquizgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val playerId: Int,
    val score: Int,
    val totalQuestions: Int,
    val timeSpent: Int,
    val createdAt: Long = System.currentTimeMillis()
)