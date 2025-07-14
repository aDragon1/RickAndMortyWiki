package self.adragon.rickandmortywiki.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.gson.Gson
import self.adragon.rickandmortywiki.data.database.CharacterDatabase
import self.adragon.rickandmortywiki.data.model.Character
import self.adragon.rickandmortywiki.data.remote.CharacterApi
import javax.inject.Inject


// https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator
@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val characterApi: CharacterApi,
    private val characterDatabase: CharacterDatabase
) : RemoteMediator<Int, Character>() {

    private val characterDAO = characterDatabase.characterDAO()

    /*
    TODO (#1 repeat):
     It's so hackish way to solve the issue, need to create an remoteKeys table,
        but, works == works
        https://youtu.be/SETnK2ny1R0?si=MnOQTmpCX4GV-2LW
     */
    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Character>
    ): MediatorResult = try {

        val curPage = when (loadType) {
            LoadType.REFRESH -> 1 // first page after refresh
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                ++currentPage
//                val lastItem = state.lastItemOrNull()
//                if (lastItem == null) return MediatorResult.Success(endOfPaginationReached = true)
//
//                lastItem.id + 1
            }
        }

        val response = characterApi.getCharacters(curPage)
        Log.d("mytag", "RemoteMediator.Load: Cur page = $curPage")
        Log.d("mytag", "RemoteMediator.Load: response size - ${response.results.size}")

//        val prevPage = if (curPage == 1) null else curPage - 1
//        val nextPage = if (response.results.isEmpty()) null else curPage + 1

        characterDatabase.withTransaction {
            if (loadType == LoadType.REFRESH) characterDAO.deleteAll()

            characterDAO.insertMany(response.results)
        }
        MediatorResult.Success(response.results.isEmpty())
    } catch (e: Exception) {
        MediatorResult.Error(e)
    }
}