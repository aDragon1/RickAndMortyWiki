package self.adragon.rickandmortywiki.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import self.adragon.rickandmortywiki.data.model.Location
import javax.inject.Inject

class CharacterConverter {

    val gson = Gson()

    @TypeConverter
    fun episodesToString(episodes: List<String>): String = gson.toJson(episodes)

    @TypeConverter
    fun stringToEpisodes(str: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(str, listType)
    }

    @TypeConverter
    fun locationToString(loc: Location): String = gson.toJson(loc)

    @TypeConverter
    fun stringToLoc(str: String): Location = gson.fromJson(str, Location::class.java)
}