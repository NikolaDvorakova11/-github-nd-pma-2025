package com.example.vanocniapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val context: Context) {

    private val dataStore = context.settingsDataStore

    // Klíče pro DataStore
    private val OPENED_DAYS = stringSetPreferencesKey("opened_days")
    private val MOCK_DATE = longPreferencesKey("mock_date")

    // --- Otevřená políčka ---
    val openedDaysFlow: Flow<Set<Int>> = dataStore.data.map {
        it[OPENED_DAYS]?.mapNotNull { dayString -> dayString.toIntOrNull() }?.toSet() ?: emptySet()
    }

    suspend fun toggleDayState(day: Int): Boolean {
        var isNowOpen = false
        dataStore.edit {
            val currentOpenedDays = it[OPENED_DAYS]?.toMutableSet() ?: mutableSetOf()
            val dayString = day.toString()

            if (currentOpenedDays.contains(dayString)) {
                currentOpenedDays.remove(dayString)
                isNowOpen = false
            } else {
                currentOpenedDays.add(dayString)
                isNowOpen = true
            }
            it[OPENED_DAYS] = currentOpenedDays
        }
        return isNowOpen
    }

    suspend fun clearOpenedDays() {
        dataStore.edit { it.remove(OPENED_DAYS) }
    }

    // --- Testovací datum ---
    val mockDateFlow: Flow<Long?> = dataStore.data.map {
        it[MOCK_DATE]
    }

    suspend fun setMockDate(dateInMillis: Long) {
        dataStore.edit { it[MOCK_DATE] = dateInMillis }
    }

    suspend fun clearMockDate() {
        dataStore.edit { it.remove(MOCK_DATE) }
    }
}
