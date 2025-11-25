package com.example.myapp013mathquizgame.data

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapp013mathquizgame.R

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Player::class, Result::class],
    version = 1,
    exportSchema = false
)
abstract class MathQuizDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun resultDao(): ResultDao
}
