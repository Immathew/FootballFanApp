package com.example.footballfanapp.di

import android.content.Context
import androidx.room.Room
import com.example.footballfanapp.data.database.TopLeaguesDao
import com.example.footballfanapp.data.database.TopLeaguesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Singleton
    @Provides
    @Named("testDb")
    fun providesInMemoryDatabase(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(
        context,
        TopLeaguesDatabase::class.java
    ).allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    @Named("testDao")
    fun providesTestDao (database: TopLeaguesDatabase): TopLeaguesDao = database.topLeaguesDao()
}