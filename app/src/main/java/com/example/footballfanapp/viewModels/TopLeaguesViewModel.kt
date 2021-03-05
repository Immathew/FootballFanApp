package com.example.footballfanapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.util.Constants.Companion.QUERY_PLAN
import com.example.footballfanapp.util.NetworkResult

class TopLeaguesViewModel(application: Application): AndroidViewModel(application) {

    fun removeUnwantedLeagues(topLeague: NetworkResult<TopLeaguesModel>): TopLeaguesModel {
        val filter = topLeague.data?.competitions?.filterNot {
            it.code == "BSA" || it.code == "CL" || it.code =="EC"
                    || it.code == "WC" || it.code == "ELC"
        }

        return TopLeaguesModel(filter!!)
    }

    fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_PLAN] = "TIER_ONE"

        return queries
    }
}