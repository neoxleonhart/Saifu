package cl.neoxcore.saifu.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.neoxcore.saifu.data.cache.database.dao.TransactionDao
import cl.neoxcore.saifu.data.cache.model.CacheTransaction

@Database(version = 1, entities = [CacheTransaction::class])
abstract class DatabaseBuilder : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
