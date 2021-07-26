package cl.neoxcore.saifu.data.cache.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.neoxcore.saifu.data.cache.preferences.Constants.ADDRESS
import cl.neoxcore.saifu.data.cache.preferences.Constants.PREFERENCES_NAME
import javax.inject.Inject

class CachePreferences @Inject constructor(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val addressKey = stringPreferencesKey(ADDRESS)

    suspend fun storeCacheAddress(value: String) {
        context.dataStore.edit { preferences ->
            preferences[addressKey] = value
        }
    }
}
