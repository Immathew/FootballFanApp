package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.R
import com.example.footballfanapp.databinding.UpcomingMatchesRowLayoutBinding
import com.example.footballfanapp.models.Match
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.CalculateDiffUtil
import com.google.android.material.card.MaterialCardView
import okhttp3.internal.trimSubstring

class UpcomingMatchesAdapter: RecyclerView.Adapter<UpcomingMatchesAdapter.MyViewHolder>() {

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
            holder.itemView.findViewById<MaterialCardView>(R.id.upcomingMatches_header_cardView).visibility = View.GONE
        } else {
            holder.itemView.findViewById<MaterialCardView>(R.id.upcomingMatches_header_cardView).visibility = View.VISIBLE
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