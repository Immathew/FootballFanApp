package com.example.footballfanapp.bindingAdapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.footballfanapp.util.DateFormatter.Companion.getDateWithServerTimeStamp
import com.google.android.material.chip.Chip

class UpcomingMatchesRowBinding {

    companion object {
        @BindingAdapter("displayMatchTime", requireAll = true)
        @JvmStatic
        fun displayMatchTime(textView: TextView, dateFromApi: String) {
            val dateToStringWithLocalGMT = dateFromApi.getDateWithServerTimeStamp()
            textView.text = dateToStringWithLocalGMT.toString().substring(11,16)
        }

        @BindingAdapter("loadClubImageFromUrl",requireAll = true)
        @JvmStatic
        fun loadClubImageFromUrl(imageView: ImageView, teamId: Int?) {
            val imageUrl = "https://crests.football-data.org/$teamId.svg"

            imageView.load(imageUrl) {

                crossfade(600)
            }
        }
    }
}