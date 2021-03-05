package com.example.footballfanapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.Constants
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_FROM
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_TO
import com.example.footballfanapp.util.NetworkResult
import kotlin.collections.HashMap

class UpcomingMatchesViewModel(application: Application) : AndroidViewModel(application) {


    private var dateFrom = ""
    private var dateTo = ""

    fun removeUnwantedLeagues(upcomingMatches: NetworkResult<UpcomingMatchesModel>): UpcomingMatchesModel {

        val filter = upcomingMatches.data!!.matches.filterNot {
            it.competition.name == "Série A" || it.competition.name == "UEFA Champions League" || it.competition.name == "Europe"
                    || it.competition.name == "FIFA World Cup" || it.competition.name == "Championship"
        }.sortedBy {
            it.competition.area?.name
        }
        return UpcomingMatchesModel(filter)
    }

    fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_DATE_FROM] = dateFrom
        queries[QUERY_DATE_TO] = dateTo

        return queries
    }

    fun setTodayDate() {
        dateFrom = ""
        dateTo = ""
    }

    fun setTomorrowDate() {
        dateFrom = "2021-03-06"
        dateTo = "2021-03-06"
    }

    fun setYesterdayDate() {
        dateFrom = "2021-03-03"
        dateTo = "2021-03-03"
    }
}