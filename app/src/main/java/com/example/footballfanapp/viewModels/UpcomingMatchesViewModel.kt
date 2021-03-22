package com.example.footballfanapp.viewModels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_FROM
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_TO
import com.example.footballfanapp.util.NetworkResult
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UpcomingMatchesViewModel(application: Application) : AndroidViewModel(application) {


    private var dateFrom = ""
    private var dateTo = ""
    var selectedChip = 0

    fun removeUnwantedLeagues(upcomingMatches: NetworkResult<UpcomingMatchesModel>): UpcomingMatchesModel {

        val filter = upcomingMatches.data!!.matches.filterNot {
            it.competition.name == "Série A" || it.competition.name == "UEFA Champions League" || it.competition.name == "Europe"
                    || it.competition.name == "FIFA World Cup" || it.competition.name == "Championship" || it.status == "POSTPONED"
                    || it.status == "CANCELED"
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

    @SuppressLint("SimpleDateFormat")
    fun setDayAfterTomorrowTwoDate() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, 3)
        val dateToQuery = SimpleDateFormat("yyyy-MM-dd").format(date.time)

        dateFrom = dateToQuery
        dateTo = dateToQuery
    }

    @SuppressLint("SimpleDateFormat")
    fun setDayAfterTomorrowDate() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, 2)
        val dateToQuery = SimpleDateFormat("yyyy-MM-dd").format(date.time)

        dateFrom = dateToQuery
        dateTo = dateToQuery
    }

    @SuppressLint("SimpleDateFormat")
    fun setTomorrowDate() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, 1)
        val dateToQuery = SimpleDateFormat("yyyy-MM-dd").format(date.time)

        dateFrom = dateToQuery
        dateTo = dateToQuery
    }

    @SuppressLint("SimpleDateFormat")
    fun setYesterdayDate() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, -1)
        val dateToQuery = SimpleDateFormat("yyyy-MM-dd").format(date.time)

        dateFrom = dateToQuery
        dateTo = dateToQuery
    }

    @SuppressLint("SimpleDateFormat")
    fun setDayBeforeYesterdayDate() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, -2)
        val dateToQuery = SimpleDateFormat("yyyy-MM-dd").format(date.time)

        dateFrom = dateToQuery
        dateTo = dateToQuery
    }

    @SuppressLint("SimpleDateFormat")
    fun setDayBeforeYesterdayTwoDate() {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, -3)
        val dateToQuery = SimpleDateFormat("yyyy-MM-dd").format(date.time)

        dateFrom = dateToQuery
        dateTo = dateToQuery
    }

}