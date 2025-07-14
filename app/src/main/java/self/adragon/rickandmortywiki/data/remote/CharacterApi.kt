package self.adragon.rickandmortywiki.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): CharacterResponse
}