package cl.neoxcore.saifu

import java.util.UUID

object BaseFactory {
    fun randomString(): String = UUID.randomUUID().toString()
}
