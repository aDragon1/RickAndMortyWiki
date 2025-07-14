package self.adragon.rickandmortywiki.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import self.adragon.rickandmortywiki.data.database.CharacterDatabase
import self.adragon.rickandmortywiki.data.model.Character
import self.adragon.rickandmortywiki.data.paging.CharacterRemoteMediator
import self.adragon.rickandmortywiki.data.paging.CharacterSearchRemoteMediator
import self.adragon.rickandmortywiki.data.remote.CharacterApi
import javax.inject.Inject

@ExperimentalPagingApi
class CharacterRepository @Inject constructor(
    private val characterApi: CharacterApi,
    private val characterDatabase: CharacterDatabase
) {

    fun getAll() = Pager(
        config = PagingConfig(20),
        remoteMediator = CharacterRemoteMediator(characterApi, characterDatabase),
        pagingSourceFactory = { characterDatabase.characterDAO().getALl() }
    ).flow

    fun search(
        name: String?, status: String?, species: String?, type: String?, gender: String?
    ): Flow<PagingData<Character>> {
        val factory = {

            val qName = "%${name ?: ""}%"
            val qStatus = "%${status ?: ""}%"
            val qSpecies = "%${species ?: ""}%"
            val qType = "%${type ?: ""}%"
            val qGender = "%${gender ?: ""}%"

            characterDatabase.characterDAO().search(qName, qStatus, qSpecies, qType, qGender)
        }

        return Pager(
            config = PagingConfig(20, initialLoadSize = 20),
            remoteMediator = CharacterSearchRemoteMediator(
                characterApi, characterDatabase, name, status, species, type, gender
            ),
            pagingSourceFactory = factory
        ).flow
    }

    fun getCharacterById(id: Int) = characterDatabase.characterDAO().getCharacterById(id)
}