package com.example.myapp013mathquizgame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {

    @Insert
    suspend fun insertResult(result: Result)

    @Query("SELECT * FROM results ORDER BY createdAt DESC")
    fun getAllResults(): Flow<List<Result>>

    @Query("SELECT * FROM results WHERE playerId = :playerId ORDER BY createdAt DESC")
    fun getResultsForPlayer(playerId: Int): Flow<List<Result>>
}