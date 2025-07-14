package self.adragon.rickandmortywiki.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import self.adragon.rickandmortywiki.data.database.dao.CharacterDAO
import self.adragon.rickandmortywiki.data.model.Character
import self.adragon.rickandmortywiki.util.Constants.CHARACTER_DATABASE

@Database(
    entities = [Character::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(CharacterConverter::class)
abstract class CharacterDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun deleteDatabase(context: Context) {
            context.deleteDatabase(CHARACTER_DATABASE)
            INSTANCE = null
        }
    }

    abstract fun characterDAO(): CharacterDAO
}