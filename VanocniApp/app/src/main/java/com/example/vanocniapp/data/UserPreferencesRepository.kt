package com.example.vanocniapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val context: Context) {

    private val dataStore = context.settingsDataStore

    // Klíč pro uložení množiny otevřených dnů (jako textových řetězců)
    private val OPENED_DAYS = stringSetPreferencesKey("opened_days")

    /**
     * Vrací Flow s množinou čísel otevřených dnů.
     */
    val openedDaysFlow: Flow<Set<Int>> = dataStore.data.map {
        // Přečteme set stringů, převedeme ho na set integerů a vrátíme.
        // Pokud je v DataStore prázdno, vrátíme prázdný set.
        it[OPENED_DAYS]?.mapNotNull { dayString -> dayString.toIntOrNull() }?.toSet() ?: emptySet()
    }

    /**
     * Přidá den do množiny otevřených dnů v DataStore.
     */
    suspend fun addOpenedDay(day: Int) {
        dataStore.edit {
            val currentOpenedDays = it[OPENED_DAYS] ?: emptySet()
            // K aktuálním dnům přidáme nový a uložíme zpět.
            it[OPENED_DAYS] = currentOpenedDays + day.toString()
        }
    }
}
