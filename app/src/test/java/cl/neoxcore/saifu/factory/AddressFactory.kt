package cl.neoxcore.saifu.factory

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.factory.BaseFactory.randomString

object AddressFactory {
    fun makeRemoteAddress() = RemoteAddress(
        private = randomString(),
        public = randomString(),
        address = randomString(),
        wif = randomString()
    )
}
