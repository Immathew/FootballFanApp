package com.example.footballfanapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.databinding.LeagueTableRowLayoutBinding
import com.example.footballfanapp.models.Standing
import com.example.footballfanapp.models.Table
import com.example.footballfanapp.util.CalculateDiffUtil

class LeagueTableAdapter : RecyclerView.Adapter<LeagueTableAdapter.MyViewHolder>() {

    private var leaguesTable = emptyList<Table>()

    class MyViewHolder(private val binding: LeagueTableRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(table: Table) {
            binding.table = table
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    LeagueTableRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTable = leaguesTable[position]

        holder.bind(currentTable)
    }

    override fun getItemCount(): Int {
        return leaguesTable.size
    }

    fun setData(newData: Standing) {
        val tablesDiffUtil = CalculateDiffUtil(leaguesTable, newData.table)
        val diffUtilResult = DiffUtil.calculateDiff(tablesDiffUtil)
        leaguesTable = newData.table
        diffUtilResult.dispatchUpdatesTo(this)
    }
}