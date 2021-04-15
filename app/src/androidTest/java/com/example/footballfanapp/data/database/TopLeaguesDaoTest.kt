package com.example.footballfanapp.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballfanapp.data.database.entities.TopLeaguesEntity
import com.example.footballfanapp.di.DatabaseModule
import com.example.footballfanapp.getOrAwaitValue
import com.example.footballfanapp.models.Area
import com.example.footballfanapp.models.Competition
import com.example.footballfanapp.models.CurrentSeason
import com.example.footballfanapp.models.TopLeaguesModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
class TopLeaguesDaoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("testDb")
    lateinit var database: TopLeaguesDatabase

    @Inject
    @Named("testDao")
    lateinit var dao: TopLeaguesDao

    @Before
    fun setupDB() {
        hiltRule.inject()

        dao = database.topLeaguesDao()
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
        database.topLeaguesDao().insertTopLeagues(topLeaguesEntity)

        // then
        val readDB = database.topLeaguesDao().readTopLeagues().getOrAwaitValue()

//        assertThat(readDB).contains(topLeaguesEntity.id)
        assertThat(readDB[0].id).isEqualTo(topLeaguesEntity.id)
        assertThat(readDB[0].topLeaguesModel).isEqualTo(topLeaguesEntity.topLeaguesModel)
        assertThat(readDB[0].topLeaguesModel.competitions).isEqualTo(topLeaguesEntity.topLeaguesModel.competitions)

    }

}


