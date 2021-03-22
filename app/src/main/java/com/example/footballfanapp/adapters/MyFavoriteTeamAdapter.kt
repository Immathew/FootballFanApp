package com.example.footballfanapp.adapters

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.R
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.databinding.MyFavoriteTeamRowLayoutBinding
import com.example.footballfanapp.ui.fragments.myFavouriteTeam.MyFavouriteTeamDirections
import com.example.footballfanapp.util.CalculateDiffUtil
import com.example.footballfanapp.viewModels.TeamDetailsViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class MyFavoriteTeamAdapter(
    private val requireActivity: FragmentActivity,
    private val teamDetailsViewModel: TeamDetailsViewModel
) : RecyclerView.Adapter<MyFavoriteTeamAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favoriteTeams = emptyList<FavoriteTeamEntity>()

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var multiSelection = false
    private var selectedTeams = arrayListOf<FavoriteTeamEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

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

        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentFavoriteTeam = favoriteTeams[position]

        holder.bind(currentFavoriteTeam)

        /**Single click listener*/
        holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteTeam_rowLayout)
            .setOnClickListener {
                if (multiSelection) {
                    applySelection(holder, currentFavoriteTeam)
                } else {
                    val action =
                        MyFavouriteTeamDirections.actionMyFavouriteTeamToTeamDetailsActivity(
                            currentFavoriteTeam.teamId
                        )
                    holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteTeam_rowLayout)
                        .findNavController().navigate(action)
                }
            }

        /**Long click listener*/
        holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteTeam_rowLayout)
            .setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this)
                    applySelection(holder, currentFavoriteTeam)
                    true
                } else if (selectedTeams.size > 0) {
                    true
                } else {
                    multiSelection = false
                    false
                }
            }
    }

    private fun applySelection(holder: MyViewHolder, currentTeam: FavoriteTeamEntity) {
        if (selectedTeams.contains(currentTeam)) {
            selectedTeams.remove(currentTeam)
            changeTeamStyle(holder, R.color.cardBackgroundColor, R.color.cardBackgroundColor)
            applyActionModeTitle()
        } else {
            selectedTeams.add(currentTeam)
            changeTeamStyle(holder, R.color.cardBackgroundColorLight, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeTeamStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteTeam_rowLayout)
            .setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))

        holder.itemView.findViewById<MaterialCardView>(R.id.favoriteTeam_cardView).strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedTeams.size) {
            0 -> mActionMode.finish()
            1 -> mActionMode.title = "Delete Team"
            else -> mActionMode.title = "Delete Teams"
        }
    }

    override fun getItemCount(): Int {
        return favoriteTeams.size
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_team_contextual_menu, menu)
        mActionMode = mode!!
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
       if (item?.itemId == R.id.deleteFavoriteTeam_menu) {
            selectedTeams.forEach {
                teamDetailsViewModel.deleteFavoriteTeam(it)
            }
           showSnackBar("${selectedTeams.size} Team/s removed from favorites")
           multiSelection = false
           selectedTeams.clear()
           mode?.finish()
       }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeTeamStyle(holder, R.color.cardBackgroundColor, R.color.cardBackgroundColor)
        }
        multiSelection = false
        selectedTeams.clear()
    }


    fun setData(newData: List<FavoriteTeamEntity>) {
        val favoriteTeamDiffUtil = CalculateDiffUtil(favoriteTeams, newData)
        val calculateDiffUtil = DiffUtil.calculateDiff(favoriteTeamDiffUtil)
        favoriteTeams = newData
        calculateDiffUtil.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}