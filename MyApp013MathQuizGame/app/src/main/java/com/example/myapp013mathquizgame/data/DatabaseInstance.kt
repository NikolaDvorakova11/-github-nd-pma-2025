package com.example.myapp013mathquizgame.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    @Volatile
    private var INSTANCE: MathQuizDatabase? = null

    fun getDatabase(context: Context): MathQuizDatabase {
        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                MathQuizDatabase::class.java,
                "math_quiz_db"
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}