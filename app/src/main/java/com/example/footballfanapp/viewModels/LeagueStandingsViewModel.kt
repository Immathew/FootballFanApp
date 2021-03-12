package com.example.footballfanapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footballfanapp.data.Repository
import com.example.footballfanapp.models.LeagueStanding
import com.example.footballfanapp.models.TopScorers
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LeagueStandingsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** RETROFIT */

    var leagueStandingResponse: MutableLiveData<NetworkResult<LeagueStanding>> =
        MutableLiveData()

    var leagueUpcomingMatchesResponse: MutableLiveData<NetworkResult<UpcomingMatchesModel>> =
        MutableLiveData()

    var leagueTopScorersResponse: MutableLiveData<NetworkResult<TopScorers>> =
        MutableLiveData()


    fun getLeagueStanding(leagueId: Int) = viewModelScope.launch {
        getStandingSafeCall(leagueId)
    }

    fun getLeagueUpcomingMatches(leagueId: Int, queries: Map<String, String>) =
        viewModelScope.launch {
            getLeagueUpcomingMatchesSafeCall(leagueId, queries)
        }

    fun getLeagueTopScorers(leagueId: Int, queries: Map<String, String>) = viewModelScope.launch {
        getLeagueTopScorersSafeCall(leagueId, queries)
    }

    private suspend fun getLeagueTopScorersSafeCall(leagueId: Int, queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getLeagueTopScorers(leagueId, queries)
                leagueTopScorersResponse.value = handleLeagueTopScorersResponse(response)
            } catch (e: Exception) {
                leagueTopScorersResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            leagueTopScorersResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleLeagueTopScorersResponse(response: Response<TopScorers>): NetworkResult<TopScorers> {
        when {
            response.code() == 404 -> {
                return NetworkResult.Error("You tried to access a resource that doesn't exist")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("You exceeded your API request quota")
            }
            response.body()!!.scorers.isNullOrEmpty() -> {
                return NetworkResult.Error("League Table is Empty")
            }
            response.isSuccessful -> {
                val leagueTopScorers = response.body()
                return NetworkResult.Success(leagueTopScorers!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private suspend fun getLeagueUpcomingMatchesSafeCall(
        leagueId: Int,
        queries: Map<String, String>
    ) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getLeagueUpcomingMatches(leagueId, queries)
                leagueUpcomingMatchesResponse.value = handleLeagueUpcomingMatchesResponse(response)
            } catch (e: Exception) {
                leagueUpcomingMatchesResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            leagueUpcomingMatchesResponse.value = NetworkResult.Error("No internet connection")
        }
    }


    private suspend fun getStandingSafeCall(leagueId: Int) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getLeagueStanding(leagueId)
                leagueStandingResponse.value = handleLeagueStandingResponse(response)
            } catch (e: Exception) {
                leagueStandingResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            leagueStandingResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleLeagueStandingResponse(response: Response<LeagueStanding>): NetworkResult<LeagueStanding> {
        when {
            response.code() == 404 -> {
                return NetworkResult.Error("You tried to access a resource that doesn't exist")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("You exceeded your API request quota")
            }
            response.body()!!.standings.isNullOrEmpty() -> {
                return NetworkResult.Error("League Table is Empty")
            }
            response.isSuccessful -> {
                val leagueStanding = response.body()
                return NetworkResult.Success(leagueStanding!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleLeagueUpcomingMatchesResponse(
        response: Response<UpcomingMatchesModel>
    ): NetworkResult<UpcomingMatchesModel> {
        when {
            response.code() == 404 -> {
                return NetworkResult.Error("You tried to access a resource that doesn't exist")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("You exceeded your API request quota")
            }
            response.body()!!.matches.isNullOrEmpty() -> {
                return NetworkResult.Error("League Table is Empty")
            }
            response.isSuccessful -> {
                val leagueUpcomingMatches = response.body()
                return NetworkResult.Success(leagueUpcomingMatches!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}