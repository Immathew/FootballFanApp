package com.example.footballfanapp.bindingAdapters

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.footballfanapp.MyApplication
import com.example.footballfanapp.models.TeamDetails
import com.example.footballfanapp.ui.LeagueStandingsActivity
import com.example.footballfanapp.ui.TeamDetailsActivity
import com.example.footballfanapp.ui.fragments.leagueTable.LeagueTableFragment
import com.example.footballfanapp.ui.fragments.topLeagues.TopLeaguesDirections
import java.lang.Exception


class TopLeaguesRowBinding {

    companion object {

        @BindingAdapter("onTopLeaguesRowClickListener")
        @JvmStatic
        fun onTopLeaguesRowClickListener(topLeaguesRowLayout: ConstraintLayout, competitionId: Int) {
            topLeaguesRowLayout.setOnClickListener {
                try {
                    val action =
                        TopLeaguesDirections.actionTopLeaguesToLeagueStandingsActivity(competitionId)
                    topLeaguesRowLayout.findNavController().navigate(action)
                } catch (e:Exception){
                    Log.d("onTopLeaguesClickLis.",e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
             imageView.load(imageUrl) {
                crossfade(200)
             }
        }
    }
}