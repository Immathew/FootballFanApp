package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.R
import com.example.footballfanapp.databinding.UpcomingMatchesRowLayoutBinding
import com.example.footballfanapp.models.Match
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.CalculateDiffUtil
import okhttp3.internal.trimSubstring

class UpcomingMatchesAdapter : RecyclerView.Adapter<UpcomingMatchesAdapter.MyViewHolder>() {

    private var upcomingMatches = emptyList<Match>()

    class MyViewHolder(private val binding: UpcomingMatchesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Match) {
            binding.match = match
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UpcomingMatchesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMatch = upcomingMatches[position]

        if (position > 0 && upcomingMatches[position - 1].competition.name?.trimSubstring() == currentMatch.competition.name?.trimSubstring()) {
            holder.itemView.findViewById<TextView>(R.id.upcomingMatchesLeagueName_textView).visibility =
                View.GONE
            holder.itemView.findViewById<ImageView>(R.id.upcomingMatches_leagueFlag_imageView).visibility =
                View.GONE
        } else {
            holder.itemView.findViewById<TextView>(R.id.upcomingMatchesLeagueName_textView).visibility =
                View.VISIBLE
            holder.itemView.findViewById<ImageView>(R.id.upcomingMatches_leagueFlag_imageView).visibility =
                View.VISIBLE
        }

        if (upcomingMatches[position].score?.fullTime?.homeTeam != null
            || upcomingMatches[position].score?.fullTime?.awayTeam != null) {
            holder.itemView.findViewById<TextView>(R.id.colon_textView).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.homeTeam_score_textView).visibility =
                View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.awayTeam_score_textView).visibility =
                View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.testingMatchTime_textView).visibility =
                View.INVISIBLE
        } else {
            holder.itemView.findViewById<TextView>(R.id.colon_textView).visibility = View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.homeTeam_score_textView).visibility =
                View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.awayTeam_score_textView).visibility =
                View.INVISIBLE
            holder.itemView.findViewById<TextView>(R.id.testingMatchTime_textView).visibility =
                View.VISIBLE
        }

        holder.bind(currentMatch)

    }

    override fun getItemCount(): Int {
        return upcomingMatches.size
    }

    fun setData(newData: UpcomingMatchesModel) {
        val competitionDiffUtil = CalculateDiffUtil(upcomingMatches, newData.matches)
        val diffUtilResult = DiffUtil.calculateDiff(competitionDiffUtil)
        upcomingMatches = newData.matches
        diffUtilResult.dispatchUpdatesTo(this)
    }
}