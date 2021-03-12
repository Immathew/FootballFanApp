package com.example.footballfanapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LeagueUpcomingMatchesViewModel(application: Application): AndroidViewModel(application) {

    var status = "SCHEDULED"

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["status"] = status
        return queries
    }

    fun setScheduledMatches() {
        status = "SCHEDULED"
    }

    fun setFinishedMatches() {
        status = "FINISHED"
    }
}