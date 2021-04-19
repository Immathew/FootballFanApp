package com.example.footballfanapp.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.data.database.entities.TopLeaguesEntity
import com.example.footballfanapp.getOrAwaitValue
import com.example.footballfanapp.models.Area
import com.example.footballfanapp.models.Competition
import com.example.footballfanapp.models.CurrentSeason
import com.example.footballfanapp.models.TopLeaguesModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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


    /**
     * Check if TopLeaguesEntity is the same in DB*/

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
        dao.insertTopLeagues(topLeaguesEntity)

        // then
        val readDB = dao.readTopLeagues().getOrAwaitValue()

//        assertThat(readDB).contains(topLeaguesEntity.id)
        assertThat(readDB[0].id).isEqualTo(topLeaguesEntity.id)
        assertThat(readDB[0].topLeaguesModel).isEqualTo(topLeaguesEntity.topLeaguesModel)
        assertThat(readDB[0].topLeaguesModel.competitions).isEqualTo(topLeaguesEntity.topLeaguesModel.competitions)
    }

    /**
     * Check if FavoriteTeam is the same in DB*/

    @Test
    fun insertFavoriteTeam_readFavoriteTeam() = runBlockingTest {
        // given favorite team
        val testFavoriteTeam = FavoriteTeamEntity(0, 55, "Manchester United")

        // when
        dao.insertFavoriteTeam(testFavoriteTeam)

        // then
        val readDB = dao.readFavoriteTeam().getOrAwaitValue()

        assertThat(readDB[0].teamName).isEqualTo(testFavoriteTeam.teamName)
    }

    /**
     * Check if deleting entities in DB works*/
    @Test
    fun insertFavoriteTeam_deleteFavoriteTeam() = runBlockingTest {
        // given favorite team
        val testFavoriteTeam = FavoriteTeamEntity(0, 55, "Manchester United")

        // when
        dao.insertFavoriteTeam(testFavoriteTeam)

        // then

        dao.deleteFavoriteTeam(testFavoriteTeam)

        val readDbAfterDeleting = dao.readFavoriteTeam().getOrAwaitValue()

        assertThat(readDbAfterDeleting).doesNotContain(testFavoriteTeam)
    }
}


