package com.makeemfall.wiki.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wiki_prefs")

/**
 * Componente del dispositivo: ALMACENAMIENTO LOCAL.
 * Guarda los favoritos en la memoria del celular con DataStore,
 * así siguen ahí aunque se cierre la app.
 */
class FavoritosStore(private val context: Context) {

    private val llave = stringSetPreferencesKey("favoritos")

    val favoritos: Flow<Set<String>> = context.dataStore.data.map { it[llave] ?: emptySet() }

    suspend fun alternar(key: String) {
        context.dataStore.edit { prefs ->
            val actuales = prefs[llave] ?: emptySet()
            prefs[llave] = if (key in actuales) actuales - key else actuales + key
        }
    }
}
