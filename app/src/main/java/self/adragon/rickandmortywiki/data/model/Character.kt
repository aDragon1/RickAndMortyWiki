package self.adragon.rickandmortywiki.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import self.adragon.rickandmortywiki.util.Constants.CHARACTER_TABLE

@Entity(tableName = CHARACTER_TABLE)
@Serializable
data class Character(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded(prefix = "origin_") val origin: Location,
    @Embedded(prefix = "location_") val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
)
