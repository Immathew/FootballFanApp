package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.databinding.TopLeaguesRowLayoutBinding
import com.example.footballfanapp.models.Competition
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.util.CalculateDiffUtil

class TopLeaguesAdapter : RecyclerView.Adapter<TopLeaguesAdapter.MyViewHolder>() {

    private var topLeagues = emptyList<Competition>()

    class MyViewHolder(private val binding: TopLeaguesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(competition: Competition) {
            binding.competition = competition
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopLeaguesRowLayoutBinding.inflate(layoutInflater, parent, false)
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
        val currentCompetition = topLeagues[position]

        holder.bind(currentCompetition)
    }

    override fun getItemCount(): Int {
       return topLeagues.size
    }
    
    fun setData(newData: TopLeaguesModel) {
        val competitionDiffUtil = CalculateDiffUtil(topLeagues, newData.competitions)
        val diffUtilResult = DiffUtil.calculateDiff(competitionDiffUtil)
        topLeagues = newData.competitions
        diffUtilResult.dispatchUpdatesTo(this)
    }

}