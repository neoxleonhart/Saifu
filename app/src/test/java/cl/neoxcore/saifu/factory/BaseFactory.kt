package cl.neoxcore.saifu.factory

import java.util.UUID

object BaseFactory {
    fun randomString(): String = UUID.randomUUID().toString()
}
