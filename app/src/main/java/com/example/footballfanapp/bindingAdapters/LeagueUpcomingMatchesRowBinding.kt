package com.example.footballfanapp.bindingAdapters

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.footballfanapp.util.DateFormatter.Companion.getDateWithServerTimeStamp

class LeagueUpcomingMatchesRowBinding {

    companion object {
        @SuppressLint("SimpleDateFormat")
        @BindingAdapter("displayMatchDate", requireAll = true)
        @JvmStatic
        fun displayMatchDate(textView: TextView, dateFromApi: String) {
            val dateToStringWithLocalGMT = dateFromApi.getDateWithServerTimeStamp()

            textView.text = dateToStringWithLocalGMT.toString().substring(0,10)

        }
    }

}