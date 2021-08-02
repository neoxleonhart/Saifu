package cl.neoxcore.saifu.data.cache.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.cache.preferences.Constants.ADDRESS
import cl.neoxcore.saifu.data.cache.preferences.Constants.BALANCE_ADDRESS
import cl.neoxcore.saifu.data.cache.preferences.Constants.BALANCE_BALANCE
import cl.neoxcore.saifu.data.cache.preferences.Constants.BALANCE_FINAL_BALANCE
import cl.neoxcore.saifu.data.cache.preferences.Constants.BALANCE_UNCONFIRMED_BALANCE
import cl.neoxcore.saifu.data.cache.preferences.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CachePreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val addressKey = stringPreferencesKey(ADDRESS)
    private val balanceAddressKey = stringPreferencesKey(BALANCE_ADDRESS)
    private val balanceBalanceKey = longPreferencesKey(BALANCE_BALANCE)
    private val balanceFinalBalanceKey = longPreferencesKey(BALANCE_FINAL_BALANCE)
    private val balanceUnconfirmedBalanceKey = longPreferencesKey(BALANCE_UNCONFIRMED_BALANCE)

    suspend fun storeCacheAddress(value: String) {
        context.dataStore.edit { preferences ->
            preferences[addressKey] = value
        }
    }

    val getCacheAddress: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[addressKey].orEmpty()
    }

    suspend fun storeCacheBalance(value: CacheBalance) {
        context.dataStore.edit { preferences ->
            preferences[balanceAddressKey] = value.address
            preferences[balanceBalanceKey] = value.balance
            preferences[balanceFinalBalanceKey] = value.finalBalance
            preferences[balanceUnconfirmedBalanceKey] = value.unconfirmedBalance
        }
    }

    val getCacheBalance: Flow<CacheBalance> = context.dataStore.data.map { preferences ->
        CacheBalance(
            address = preferences[balanceAddressKey].orEmpty(),
            balance = preferences[balanceBalanceKey] ?: 0,
            finalBalance = preferences[balanceFinalBalanceKey] ?: 0,
            unconfirmedBalance = preferences[balanceUnconfirmedBalanceKey] ?: 0
        )
    }
}
