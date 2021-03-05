package com.example.footballfanapp.bindingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.footballfanapp.util.DateFormatter.Companion.getDateWithServerTimeStamp
import com.google.android.material.chip.Chip

class UpcomingMatchesRowBinding {

    companion object {
        @BindingAdapter("displayMatchTime", requireAll = true)
        @JvmStatic
        fun updateMatchTime(textView: TextView, dateFromApi: String) {
            val dateToStringWithLocalGMT = dateFromApi.getDateWithServerTimeStamp()
            textView.text = dateToStringWithLocalGMT.toString().substring(11,16)
        }
        @BindingAdapter("updateDateOnChip", requireAll = true)
        @JvmStatic
        fun updateDateOnChip(textView: Chip, dateFromApi: String?) {
            val dateToStringWithLocalGMT = dateFromApi?.getDateWithServerTimeStamp()
            textView.text = dateToStringWithLocalGMT.toString().substring(5,9)
        }
    }
}