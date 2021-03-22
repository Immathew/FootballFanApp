package com.example.footballfanapp.bindingAdapters

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.footballfanapp.ui.fragments.myFavouriteTeam.MyFavouriteTeamDirections
import java.lang.Exception

class MyFavoriteTeamBindingAdapter {

    companion object {

        @BindingAdapter("onFavoriteTeamRowClickListener")
        @JvmStatic
        fun onFavoriteTeamRowClickListener(favoriteTeamRowLayout: ConstraintLayout, teamId: Int) {
            favoriteTeamRowLayout.setOnClickListener {
                try {
                    val action =
                        MyFavouriteTeamDirections.actionMyFavouriteTeamToTeamDetailsActivity(teamId)
                    favoriteTeamRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("FavoriteTeamNavigation", e.message.toString())
                }
            }
        }
    }
}