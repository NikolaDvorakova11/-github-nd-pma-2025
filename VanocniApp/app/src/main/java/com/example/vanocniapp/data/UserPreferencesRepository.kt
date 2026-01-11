package com.example.vanocniapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Repository je třída, která izoluje logiku ukládání dat od zbytku aplikace (UI).
class UserPreferencesRepository(private val context: Context) {

    // Připojíme se k bráně DataStore, kterou jsme definovali v minulém souboru
    private val dataStore = context.settingsDataStore

    // KLÍČE pro DataStore
    // Každá hodnota v paměti musí mít svůj unikátní název (klíč).
    // stringSetPreferencesKey ukládá seznam unikátních textů (ID dnů).
    // longPreferencesKey ukládá velké číslo (datum v milisekundách).
    private val OPENED_DAYS = stringSetPreferencesKey("opened_days")
    private val MOCK_DATE = longPreferencesKey("mock_date")

    // OTEVŘENÁ POLÍČKA
    //Flow je "potok" dat. Kdykoliv se v paměti něco změní, tento potok automaticky pošle nová data všem, kteří ho "poslouchají" (např. CalendarFragment).
    val openedDaysFlow: Flow<Set<Int>> = dataStore.data.map {
        // DataStore ukládá Stringy, my ale chceme čísla (Int). Tady je převádíme.
        it[OPENED_DAYS]?.mapNotNull { dayString -> dayString.toIntOrNull() }?.toSet() ?: emptySet()
    }

    // Funkce, která políčko buď otevře, nebo zavře (přepínač)
    // 'suspend' znamená, že funkce běží na pozadí a neblokuje mobil
    suspend fun toggleDayState(day: Int): Boolean {
        var isNowOpen = false
        dataStore.edit {
            // Načteme aktuálně otevřené dny
            val currentOpenedDays = it[OPENED_DAYS]?.toMutableSet() ?: mutableSetOf()
            val dayString = day.toString()

            if (currentOpenedDays.contains(dayString)) {
                currentOpenedDays.remove(dayString)     // Pokud už tam byl, smaže daný den
                isNowOpen = false
            } else {
                currentOpenedDays.add(dayString)    // Pokud tam nebyl, přidáme ho
                isNowOpen = true
            }
            // Uložíme aktualizovaný seznam zpět do paměti mobilu
            it[OPENED_DAYS] = currentOpenedDays
        }
        return isNowOpen
    }

    // Smaže úplně všechna uložená políčka (použito v SettingsFragmentu)
    suspend fun clearOpenedDays() {
        dataStore.edit { it.remove(OPENED_DAYS) }
    }

    // TESTOVACÍ DATUM
    // Posílá aktuálně nastavené testovací datum (pokud nějaké existuje)
    val mockDateFlow: Flow<Long?> = dataStore.data.map {
        it[MOCK_DATE]
    }

    // Uloží vybrané datum jako číslo (milisekundy)
    suspend fun setMockDate(dateInMillis: Long) {
        dataStore.edit { it[MOCK_DATE] = dateInMillis }
    }

    // Smaže testovací datum a aplikace začne používat reálný čas
    suspend fun clearMockDate() {
        dataStore.edit { it.remove(MOCK_DATE) }
    }
}
