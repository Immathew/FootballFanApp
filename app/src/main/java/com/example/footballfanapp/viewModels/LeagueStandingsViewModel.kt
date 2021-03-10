package com.example.footballfanapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footballfanapp.data.Repository
import com.example.footballfanapp.models.LeagueStanding
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

    fun getLeagueStanding(leagueId: Int) = viewModelScope.launch {
        getStandingSafeCall(leagueId)
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