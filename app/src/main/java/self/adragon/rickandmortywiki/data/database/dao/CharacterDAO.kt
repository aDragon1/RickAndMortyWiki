package self.adragon.rickandmortywiki.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import self.adragon.rickandmortywiki.data.model.Character

@Dao
interface CharacterDAO {
    @Query("SELECT * FROM characters ORDER by id")
    fun getALl(): PagingSource<Int, Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(characters: List<Character>)

    @Query("DELETE FROM characters")
    suspend fun deleteAll()

    @Query(
        """
    SELECT * FROM characters 
    WHERE 
        (name LIKE :name) AND (status LIKE :status) AND (species LIKE :species) 
    AND
        (type LIKE :type) AND (gender LIKE :gender) 
"""
    )
    fun search(
        name: String, status: String, species: String, type: String, gender: String
    ): PagingSource<Int, Character>

    @Query("SELECT * FROM characters WHERE id = :id ")
    fun getCharacterById(id: Int): Character
}

