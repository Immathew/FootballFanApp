package com.example.footballfanapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.footballfanapp.data.Repository
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var topLeaguesResponse: MutableLiveData<NetworkResult<TopLeaguesModel>> = MutableLiveData()

    fun getTopLeagues(queries: Map<String, String>) = viewModelScope.launch {
        getTopLeaguesSafeCall(queries)
    }

    fun getUpcomingMatches(queries: Map<String, String>) = viewModelScope.launch {
        getUpcomingMatchesSafeCall(queries)
    }

    private fun getUpcomingMatchesSafeCall(queries: Map<String, String>) {
        TODO("Not yet implemented")
    }


    private suspend fun getTopLeaguesSafeCall(queries: Map<String, String>) {
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTopLeagues(queries)
                topLeaguesResponse.value = handleTopLeaguesResponse(response)
            } catch (e: Exception) {
                topLeaguesResponse.value = NetworkResult.Error("Some kind of error")
            }
        } else {
            topLeaguesResponse.value = NetworkResult.Error("No internet connection")
        }
    }

    private fun handleTopLeaguesResponse(response: Response<TopLeaguesModel>): NetworkResult<TopLeaguesModel>? {
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
//                val filteredTopLeagues = removeUnwantedLeagues(topLeagues!!)
                return NetworkResult.Success(topLeagues!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

//    private fun removeUnwantedLeagues(topLeague: TopLeaguesModel): TopLeaguesModel {
//        val filtered = topLeague.competitions.filterNot {
//            it.code == "BSA" || it.code == "CL" || it.code =="EC"
//                    || it.code == "WC" || it.code == "ELC"
//        }
//        val filtered2 = topLeague.competitions.sortedBy {
//            it.code
//        }
//        return TopLeaguesModel(filtered2)
//    }

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