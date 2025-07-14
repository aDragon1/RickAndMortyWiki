package self.adragon.rickandmortywiki.data.remote

import self.adragon.rickandmortywiki.data.model.Character

data class CharacterResponse(
    val info: CharacterResponseInfo,
    val results: List<Character>
)

data class CharacterResponseInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String,
)