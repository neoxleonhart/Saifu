package cl.neoxcore.saifu.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cl.neoxcore.saifu.data.cache.database.Constants.TRANSACTION_TABLE

@Entity(tableName = TRANSACTION_TABLE)
data class CacheTransaction(
    @PrimaryKey
    val id: String,
    val date: String,
    val total: Long
)
