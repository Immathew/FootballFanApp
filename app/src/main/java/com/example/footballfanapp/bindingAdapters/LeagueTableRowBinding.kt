package com.example.footballfanapp.bindingAdapters

import android.content.Intent
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.footballfanapp.ui.LeagueStandingsActivity
import com.example.footballfanapp.ui.TeamDetailsActivity
import java.lang.Exception

class LeagueTableRowBinding {

    companion object {

        @BindingAdapter("onTableRowClickListener")
        @JvmStatic
        fun onTableRowClickListener(leagueTableRowLayout: ConstraintLayout, teamId: Int) {
            leagueTableRowLayout.setOnClickListener {
                try {
//                    val action =
//                        LeagueTableFragmentDirections.actionLeagueTableFragmentToTeamDetailsActivity2(teamId)
//                    leagueTableRowLayout.findNavController().navigate(action)

                    val intent = Intent(leagueTableRowLayout.context, TeamDetailsActivity::class.java).apply {
                        putExtra("teamId", teamId)
                    }
                    startActivity(leagueTableRowLayout.context, intent, null)
                } catch (e:Exception){
                    Log.e("LeagueTableOnClickList", e.toString())
                }
            }
        }
    }
}