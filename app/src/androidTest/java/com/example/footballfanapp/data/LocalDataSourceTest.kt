package com.example.footballfanapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.footballfanapp.data.database.TopLeaguesDao
import com.example.footballfanapp.data.database.TopLeaguesDatabase
import com.example.footballfanapp.data.database.entities.TopLeaguesEntity
import com.example.footballfanapp.getOrAwaitValue
import com.example.footballfanapp.models.Area
import com.example.footballfanapp.models.Competition
import com.example.footballfanapp.models.CurrentSeason
import com.example.footballfanapp.models.TopLeaguesModel
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest : TestCase() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: LocalDataSource
    private lateinit var database: TopLeaguesDatabase
    private lateinit var dao: TopLeaguesDao


    @Before
    fun setupDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TopLeaguesDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.topLeaguesDao()

        localDataSource =
            LocalDataSource(
                dao
            )
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTopLeagues_readTopLeagues() = runBlockingTest {

        // Given TopLeaguesEntity
        val area = Area("url", "name")
        val currentSeason = CurrentSeason("endDate", "startDate")
        val competition = Competition(area, "code", currentSeason, 1, "name")
        val competitionList = mutableListOf<Competition>()
        competitionList.add(competition)
        val topLeaguesModel = TopLeaguesModel(competitionList)
        val topLeaguesEntity = TopLeaguesEntity(topLeaguesModel)

        // when
        localDataSource.insertTopLeagues(topLeaguesEntity)

        // then
        val readDB = localDataSource.readTopLeagues().getOrAwaitValue()

        assertThat(readDB[0].id).isEqualTo(topLeaguesEntity.id)
        assertThat(readDB[0].topLeaguesModel).isEqualTo(topLeaguesEntity.topLeaguesModel)
        assertThat(readDB[0].topLeaguesModel.competitions)
            .isEqualTo(topLeaguesEntity.topLeaguesModel.competitions)

    }
}