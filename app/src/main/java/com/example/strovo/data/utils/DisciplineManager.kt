package com.example.strovo.data.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.strovo.data.model.Discipline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("discipline_preferences")

class DisciplineManager(private val context: Context) {
    private val FAVORITE_DISCIPLINES = stringSetPreferencesKey("favorite_disciplines")

     suspend fun saveSelectedDisciplines(disciplines: List<Discipline>) {
         context.dataStore.edit { prefs ->
             prefs[FAVORITE_DISCIPLINES] = disciplines.map { it.name }.toSet()
         }
    }

    suspend fun getSelectedDisciplines(): List<Discipline> {
        return context.dataStore.data.map { prefs ->
            prefs[FAVORITE_DISCIPLINES]
                ?.map { Discipline.valueOf(it) }
                ?.toList()
                ?: emptyList()
        }.firstOrNull() ?: emptyList()
    }
}