package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val remote: Remote) : Repository {
    override fun generateAddress(): Flow<String> = flow {
        emit(remote.generateAddress().address.orEmpty())
    }
}
