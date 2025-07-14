package self.adragon.rickandmortywiki.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import self.adragon.rickandmortywiki.data.model.CharacterSearchQuery
import self.adragon.rickandmortywiki.data.repo.CharacterRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class CharacterViewModel
@Inject constructor(val characterRepository: CharacterRepository) : ViewModel() {

    val currentQuery = MutableStateFlow(CharacterSearchQuery())

    @OptIn(ExperimentalCoroutinesApi::class)
    val characters = currentQuery.flatMapLatest { q ->
        if (q.isEmpty()) characterRepository.getAll()
        else characterRepository.search(q.name, q.status, q.species, q.type, q.gender)
    }.cachedIn(viewModelScope)

    /* TODO (#3): It will create a lot of throw-away objects,
        but each object weights around 50-60 bytes + GC clear it anyway(?):
            5 nullable string by 8 byte each (x86_64 ARCH JVM) + object header - 12-16 bytes.
            Buy more RAM, I suppose?

            How to fix: make CharacterSearchQuery fields `var` instead of `val`
                and reuse same object over and over again
     */
    fun setSearchQueryName(pName: String?) {
        currentQuery.value = currentQuery.value.copy(name = pName)
    }

    fun setSearchQueryFilter(
        status: String? = null, species: String? = null,
        type: String? = null, gender: String? = null,
    ) {
        val curName = currentQuery.value.name
        currentQuery.value = CharacterSearchQuery(curName, status, species, type, gender)
    }
}