package com.delarax.dd5cv.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.delarax.dd5cv.models.preferences.DarkThemePreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val DATA_STORE_NAME = "dd5cv_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_NAME
)

// Doesn't need @Singleton because `dataStore` returned from
// `by preferencesDataStore(...)` is a singleton
class PreferencesRepo @Inject constructor(
    @ApplicationContext private val appContext: Context
){
    private val dataStore: DataStore<Preferences> = appContext.dataStore

    val darkThemeFlow: Flow<DarkThemePreference> = dataStore.data
        .catch  { e ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            when(it[DARK_THEME_KEY]) {
                DarkThemePreference.OFF.name -> DarkThemePreference.OFF
                DarkThemePreference.ON.name -> DarkThemePreference.ON
                else -> DarkThemePreference.MATCH_SYSTEM
            }
        }

    suspend fun setDarkThemePreference(preference: DarkThemePreference) {
        dataStore.edit {
            it[DARK_THEME_KEY] = preference.name
        }
    }

    companion object {
        val DARK_THEME_KEY = stringPreferencesKey("dark_them_key")
    }
}