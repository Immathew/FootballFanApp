package com.example.footballfanapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.Constants
import com.example.footballfanapp.util.NetworkResult

class TeamMatchesViewModel(application: Application): AndroidViewModel(application) {

    fun removeUnwantedLeagues(upcomingMatches: NetworkResult<UpcomingMatchesModel>): UpcomingMatchesModel {

        val filter = upcomingMatches.data!!.matches.filterNot {
            it.competition.name == "Série A" || it.competition.name == "UEFA Champions League" || it.competition.name == "Europe"
                    || it.competition.name == "FIFA World Cup" || it.competition.name == "Championship" || it.status == "POSTPONED"
                    || it.status == "CANCELED"
        }
        return UpcomingMatchesModel(filter)
    }

    var status = "SCHEDULED"

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_STATUS] = status
        return queries
    }

    fun setScheduledMatches() {
        status = "SCHEDULED"
    }

    fun setFinishedMatches() {
        status = "FINISHED"
    }
}