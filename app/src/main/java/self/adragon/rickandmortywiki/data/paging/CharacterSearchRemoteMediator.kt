package self.adragon.rickandmortywiki.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import self.adragon.rickandmortywiki.data.database.CharacterDatabase
import self.adragon.rickandmortywiki.data.model.Character
import self.adragon.rickandmortywiki.data.remote.CharacterApi


// https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator
@OptIn(ExperimentalPagingApi::class)
class CharacterSearchRemoteMediator(
    private val characterApi: CharacterApi,
    private val characterDatabase: CharacterDatabase,
    private val name: String? = null,
    private val status: String? = null,
    private val species: String? = null,
    private val type: String? = null,
    private val gender: String? = null,
) : RemoteMediator<Int, Character>() {

    private val characterDAO = characterDatabase.characterDAO()

    /*
    TODO (#1):
     It's so hackish way to solve the issue, need to create an remoteKeys table,
        but, works == works
        https://youtu.be/SETnK2ny1R0?si=MnOQTmpCX4GV-2LW
     */
    private var currentPage = 1
    private var maxPage = Int.MAX_VALUE

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Character>
    ): MediatorResult = try {

        Log.d("mytag", "Current search page - $currentPage")

        val curPage = when (loadType) {
            LoadType.REFRESH -> 1 // first page after refresh
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> if (currentPage < maxPage) ++currentPage else
                return MediatorResult.Success(endOfPaginationReached = true)
        }

        val response = characterApi.getCharacters(curPage, name, status, species, type, gender)
        maxPage = response.info.pages


        characterDatabase.withTransaction { characterDAO.insertMany(response.results) }
        MediatorResult.Success(response.results.isEmpty())
    } catch (e: Exception) {
        MediatorResult.Error(e)
    }
}