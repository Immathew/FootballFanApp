package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.R
import com.example.footballfanapp.databinding.LeagueUpcomingMatchesRowLayoutBinding
import com.example.footballfanapp.models.Match
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.CalculateDiffUtil

class LeagueUpcomingMatchesAdapter :
    RecyclerView.Adapter<LeagueUpcomingMatchesAdapter.MyViewHolder>() {

    private var leagueUpcomingMatches = emptyList<Match>()

    class MyViewHolder(private val binding: LeagueUpcomingMatchesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Match) {
            binding.match = match
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    LeagueUpcomingMatchesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentLeagueMatch = leagueUpcomingMatches[position]

        if (position > 0 && leagueUpcomingMatches[position - 1].utcDate?.substring(0,10) == currentLeagueMatch.utcDate?.substring(0,10)) {
            holder.itemView.findViewById<TextView>(R.id.leagueUpcomingMatchesDate_textView).visibility =
                View.GONE
        } else {
            holder.itemView.findViewById<TextView>(R.id.leagueUpcomingMatchesDate_textView).visibility =
                View.VISIBLE
        }

        if (leagueUpcomingMatches[position].score?.fullTime?.homeTeam != null
            || leagueUpcomingMatches[position].score?.fullTime?.awayTeam != null) {
            holder.itemView.findViewById<TextView>(R.id.leagueColon_textView).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.leagueHomeTeam_score_textView).visibility =
                View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.leagueAwayTeam_score_textView).visibility =
                View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.leagueMatchTime_textView).visibility =
                View.INVISIBLE
        } else {
            holder.itemView.findViewById<TextView>(R.id.leagueColon_textView).visibility = View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.leagueHomeTeam_score_textView).visibility =
                View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.leagueAwayTeam_score_textView).visibility =
                View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.leagueMatchTime_textView).visibility =
                View.VISIBLE
        }
        holder.bind(currentLeagueMatch)
    }

    override fun getItemCount(): Int {
        return leagueUpcomingMatches.size
    }

    fun setData(newData: UpcomingMatchesModel) {
        val competitionDiffUtil = CalculateDiffUtil(leagueUpcomingMatches, newData.matches)
        val diffUtilResult = DiffUtil.calculateDiff(competitionDiffUtil)
        leagueUpcomingMatches = newData.matches
        diffUtilResult.dispatchUpdatesTo(this)
    }
}