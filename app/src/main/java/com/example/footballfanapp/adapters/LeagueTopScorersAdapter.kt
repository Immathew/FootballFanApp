package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.R
import com.example.footballfanapp.databinding.LeagueTopScorersRowLayoutBinding
import com.example.footballfanapp.models.Scorer
import com.example.footballfanapp.models.TopScorers
import com.example.footballfanapp.util.CalculateDiffUtil

class LeagueTopScorersAdapter : RecyclerView.Adapter<LeagueTopScorersAdapter.MyViewHolder>() {

    private var scorers = emptyList<Scorer>()

    class MyViewHolder(private val binding: LeagueTopScorersRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(scorer: Scorer) {
            binding.scorer = scorer
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    LeagueTopScorersRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentScorer = scorers[position]
        holder.itemView.findViewById<TextView>(R.id.scorerPosition).text = (position +1).toString()
        holder.bind(currentScorer)
    }

    override fun getItemCount(): Int {
        return scorers.size
    }

    fun setData(newData: TopScorers) {
        val scorersDiffUtil = CalculateDiffUtil(scorers, newData.scorers)
        val diffUtilResult = DiffUtil.calculateDiff(scorersDiffUtil)
        scorers = newData.scorers
        diffUtilResult.dispatchUpdatesTo(this)
    }
}