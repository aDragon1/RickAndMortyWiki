package self.adragon.rickandmortywiki.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import self.adragon.rickandmortywiki.data.database.CharacterDatabase
import self.adragon.rickandmortywiki.util.Constants.CHARACTER_DATABASE
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCharacterDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CharacterDatabase::class.java,
        CHARACTER_DATABASE
    ).setQueryCallback({ sqlQuery, bindArgs ->
        Log.d("RoomSQL", "Query: $sqlQuery | Args: $bindArgs")
    }, Executors.newSingleThreadExecutor())
        .build()
}