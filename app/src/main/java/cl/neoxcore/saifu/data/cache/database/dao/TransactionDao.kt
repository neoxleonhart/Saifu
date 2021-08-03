package cl.neoxcore.saifu.data.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cl.neoxcore.saifu.data.cache.database.Constants.TRANSACTION_TABLE
import cl.neoxcore.saifu.data.cache.model.CacheTransaction

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cacheTransaction: CacheTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cacheTransactions: List<CacheTransaction>)

    @Transaction
    @Query("SELECT * FROM $TRANSACTION_TABLE")
    suspend fun getAll(): List<CacheTransaction>

    @Query("DELETE FROM $TRANSACTION_TABLE")
    suspend fun deleteAll()
}
