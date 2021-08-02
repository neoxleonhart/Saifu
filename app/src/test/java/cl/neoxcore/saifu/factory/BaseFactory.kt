package cl.neoxcore.saifu.factory

import java.util.Random
import java.util.UUID

object BaseFactory {
    fun randomString(): String = UUID.randomUUID().toString()
    fun randomLong(): Long = Random().nextLong()
    fun randomInt(): Int = Random().nextInt()
    fun randomBoolean(): Boolean = Random().nextBoolean()
}
