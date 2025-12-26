package com.example.vanocniapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Vytvoří jedinou instanci DataStore pro celou aplikaci, dostupnou přes context
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
