package com.example.footballfanapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.footballfanapp.data.Repository
import com.example.footballfanapp.data.database.TopLeaguesEntity
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE*/

    var readTopLeagues: LiveData<List<TopLeaguesEntity>> =
        repository.local.readDatabase().asLiveData()

    private fun insertTopLeagues(topLeaguesEntity: TopLeaguesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertTopLeagues(topLeaguesEntity)
        }


    /** RETROFIT */
    var topLeaguesResponse: MutableLiveData<NetworkResult<TopLeaguesModel>> = MutableLiveData()
    var upcomingMatchesResponse: MutableLiveData<NetworkResult<UpcomingMatchesModel>> =
        MutableLiveData()

    fun getTopLeagues(queries: Map<String, String>) = viewModelScope.launch {
        getTopLeaguesSafeCall(queries)
    }

    fun getUpcomingMatches(queries: Map<String, String>) = viewModelScope.launch {
        getUpcomingMatchesSafeCall(queries)
    }

    private suspend fun getUpcomingMatchesSafeCall(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getUpcomingMatches(queries)
                upcomingMatchesResponse.value = handleUpcomingMatchesResponse(response)
            } catch (e: Exception) {
                upcomingMatchesResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            upcomingMatchesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleUpcomingMatchesResponse(response: Response<UpcomingMatchesModel>): NetworkResult<UpcomingMatchesModel> {
        when {
            response.code() == 404 -> {
                return NetworkResult.Error("You tried to access a resource that doesn't exist")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("You exceeded your API request quota")
            }
            response.body()!!.matches.isNullOrEmpty() -> {
                return NetworkResult.Error("Our Top Leagues are not playing on this day :( ")
            }
            response.isSuccessful -> {
                val upcomingMatches = response.body()
                return NetworkResult.Success(upcomingMatches!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


    private suspend fun getTopLeaguesSafeCall(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTopLeagues(queries)
                topLeaguesResponse.value = handleTopLeaguesResponse(response)

                val topLeaguesModel = topLeaguesResponse.value!!.data
                if (topLeaguesModel != null) {
                    offlineCacheTopLeagues(topLeaguesModel)
                }
            } catch (e: Exception) {
                topLeaguesResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            topLeaguesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun offlineCacheTopLeagues(topLeaguesModel: TopLeaguesModel) {
        val topLeaguesEntity = TopLeaguesEntity(topLeaguesModel)
        insertTopLeagues(topLeaguesEntity)
    }

    private fun handleTopLeaguesResponse(response: Response<TopLeaguesModel>): NetworkResult<TopLeaguesModel> {
        when {
            response.code() == 404 -> {
                return NetworkResult.Error("You tried to access a resource that doesn't exist")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("You exceeded your API request quota")
            }
            response.body()!!.competitions.isNullOrEmpty() -> {
                return NetworkResult.Error("Competitions not found")
            }
            response.isSuccessful -> {
                val topLeagues = response.body()
                return NetworkResult.Success(topLeagues!!)
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