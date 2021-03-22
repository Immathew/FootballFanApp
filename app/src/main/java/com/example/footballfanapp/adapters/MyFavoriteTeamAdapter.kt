package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.databinding.MyFavoriteTeamRowLayoutBinding
import com.example.footballfanapp.util.CalculateDiffUtil

class MyFavoriteTeamAdapter : RecyclerView.Adapter<MyFavoriteTeamAdapter.MyViewHolder>() {

    private var favoriteTeams = emptyList<FavoriteTeamEntity>()

    class MyViewHolder(private val binding: MyFavoriteTeamRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteTeamEntity: FavoriteTeamEntity) {
            binding.favoriteTeamEntity = favoriteTeamEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MyFavoriteTeamRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFavoriteTeam = favoriteTeams[position]

        holder.bind(currentFavoriteTeam)
    }

    override fun getItemCount(): Int {
        return favoriteTeams.size
    }

    fun setData(newData: List<FavoriteTeamEntity>) {
        val favoriteTeamDiffUtil = CalculateDiffUtil(favoriteTeams, newData)
        val calculateDiffUtil = DiffUtil.calculateDiff(favoriteTeamDiffUtil)
        favoriteTeams = newData
        calculateDiffUtil.dispatchUpdatesTo(this)
    }
}