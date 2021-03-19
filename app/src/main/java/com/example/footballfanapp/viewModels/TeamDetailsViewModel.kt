package com.example.footballfanapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.footballfanapp.data.Repository
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.models.TeamDetails
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /**Room Database*/

    val readFavoriteTeamEntity: LiveData<List<FavoriteTeamEntity>> =
        repository.local.readFavoriteTeam().asLiveData()

    fun insertFavoriteTeam(favoriteTeamEntity: FavoriteTeamEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFavoriteTeam(favoriteTeamEntity)
    }

    fun deleteFavoriteTeam(favoriteTeamEntity: FavoriteTeamEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteFavoritesTeam(favoriteTeamEntity)
    }

    /**Retrofit*/

    val teamDetailsResponse: MutableLiveData<NetworkResult<TeamDetails>> = MutableLiveData()
    val teamUpcomingMatchesResponse: MutableLiveData<NetworkResult<UpcomingMatchesModel>> =
        MutableLiveData()

    fun getTeamDetails(teamId: Int) = viewModelScope.launch {
        getTeamDetailsSafeCall(teamId)
    }

    fun getTeamUpcomingMatches(teamId: Int, queries: Map<String, String>) = viewModelScope.launch {
        getTeamUpcomingMatchesSafeCall(teamId, queries)
    }

    private suspend fun getTeamUpcomingMatchesSafeCall(teamId: Int, queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTeamUpcomingMatches(teamId, queries)
                teamUpcomingMatchesResponse.value = handleTeamUpcomingMatchesResponse(response)
            } catch (e: Exception) {
                teamUpcomingMatchesResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            teamUpcomingMatchesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleTeamUpcomingMatchesResponse(response: Response<UpcomingMatchesModel>): NetworkResult<UpcomingMatchesModel> {
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
                val teamUpcomingMatches = response.body()

                return NetworkResult.Success(teamUpcomingMatches!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private suspend fun getTeamDetailsSafeCall(teamId: Int) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTeamDetails(teamId)
                teamDetailsResponse.value = handleTeamDetailsResponse(response)
            } catch (e: Exception) {
                teamDetailsResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            teamDetailsResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleTeamDetailsResponse(response: Response<TeamDetails>): NetworkResult<TeamDetails> {
        when {
            response.code() == 404 -> {
                return NetworkResult.Error("You tried to access a resource that doesn't exist")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("You exceeded your API request quota")
            }
            response.body()!!.squad.isNullOrEmpty() -> {
                return NetworkResult.Error("League Table is Empty")
            }
            response.isSuccessful -> {
                val teamDetails = response.body()
                Log.e("TeamDETAILS", teamDetails.toString())
                return NetworkResult.Success(teamDetails!!)
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