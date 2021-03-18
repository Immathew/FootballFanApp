package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.R
import com.example.footballfanapp.databinding.TeamSquadRowLayoutBinding
import com.example.footballfanapp.models.Player
import com.example.footballfanapp.models.TeamDetails
import com.example.footballfanapp.util.CalculateDiffUtil

class TeamSquadAdapter : RecyclerView.Adapter<TeamSquadAdapter.MyViewHolder>() {

    private var teamSquad = emptyList<Player>()

    class MyViewHolder(private val binding: TeamSquadRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.player = player
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TeamSquadRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPlayer = teamSquad[position]

        holder.itemView.findViewById<TextView>(R.id.teamSquad_position).text = (position+1).toString()

        holder.bind(currentPlayer)
    }

    override fun getItemCount(): Int {
        return teamSquad.size
    }

    fun setData(newData: TeamDetails) {
        val playersDiffUtil = CalculateDiffUtil(teamSquad, newData.squad)
        val diffUtilResult = DiffUtil.calculateDiff(playersDiffUtil)
        teamSquad = newData.squad
        diffUtilResult.dispatchUpdatesTo(this)
    }
}