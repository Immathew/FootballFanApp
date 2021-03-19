package com.example.footballfanapp.bindingAdapters

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import coil.load
import com.example.footballfanapp.ui.TeamDetailsActivity
import com.example.footballfanapp.util.DateFormatter.Companion.getDateWithServerTimeStamp
import java.lang.Exception

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

        @BindingAdapter("onTeamNameClickListener")
        @JvmStatic
        fun onTeamNameClickListener(teamName: TextView, teamId: Int) {
            teamName.setOnClickListener {
                try {
                    val intent = Intent(teamName.context, TeamDetailsActivity::class.java).apply {
                        putExtra("teamId", teamId)
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(teamName.context, intent, null)
                } catch (e: Exception){
                    Log.e("teamNameOnClickL.", e.toString())
                }
            }
        }
    }
}