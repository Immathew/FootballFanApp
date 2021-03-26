package com.example.footballfanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.PagerAdapter
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.databinding.ActivityTeamDeteailsBinding
import com.example.footballfanapp.ui.fragments.teamMatches.TeamMatchesFragment
import com.example.footballfanapp.ui.fragments.teamSquad.TeamSquadFragment
import com.example.footballfanapp.ui.fragments.teamWebsite.TeamWebsiteFragment
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TeamDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamDetailsActivity : AppCompatActivity() {

    private val teamDetailsViewModel: TeamDetailsViewModel by viewModels()
    private val args by navArgs<TeamDetailsActivityArgs>()

    private lateinit var binding: ActivityTeamDeteailsBinding

    private var teamId = 0
    private var teamName = ""
    private var teamSavedToFavorites = false
    private var savedTeamId = 0
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent

        teamId = intent.getIntExtra("teamId", args.teamId)
        teamName = intent.getStringExtra("teamName").toString()

        Log.e("TEAM_ID", teamId.toString())

        requestApiData(teamId)

        binding = ActivityTeamDeteailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.teamDetailsToolbar)
        binding.teamDetailsToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(TeamSquadFragment())
        fragments.add(TeamMatchesFragment())
        fragments.add(TeamWebsiteFragment())

        val titles = ArrayList<String>()
        titles.add("Squad")
        titles.add("Matches")
        titles.add("Website")

        val teamDetailsBundle = Bundle()
        teamDetailsBundle.putInt("teamId", teamId)

        val pagerAdapter = PagerAdapter(
            teamDetailsBundle,
            fragments,
            this
        )

        binding.teamDetailsViewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(
            binding.teamDetailsTabLayout,
            binding.teamDetailsViewPager2
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()

        binding.teamDetailsViewPager2.reduceDragSensitivity()
    }

    private fun requestApiData(teamId: Int ) {

        teamDetailsViewModel.getTeamDetails(teamId)

        teamDetailsViewModel.teamDetailsResponse.observe(this, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val teamDetailsResponse = response.data!!
                    binding.teamDetails = teamDetailsResponse
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        this,
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{}
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.team_details_menu, menu)
        menuItem = menu!!.findItem(R.id.saveToFavorites_menu)
        checkSavedFavoriteTeam(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.saveToFavorites_menu) {
            if (teamSavedToFavorites) {
                removeFavoriteTeam(item)
            } else {
                saveToFavorites(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoriteTeamEntity =
            FavoriteTeamEntity(
                0,
                teamId,
                teamName
            )
        teamDetailsViewModel.insertFavoriteTeam(favoriteTeamEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar(getString(R.string.team_added_to_favorites))
        teamSavedToFavorites = true
    }

    private fun removeFavoriteTeam(item: MenuItem) {
        val favoriteTeamEntity =
            FavoriteTeamEntity(
                savedTeamId,
                teamId,
                teamName
            )
        teamDetailsViewModel.deleteFavoriteTeam(favoriteTeamEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar(getString(R.string.team_removed_from_favorites))
        teamSavedToFavorites = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.teamDetailsActivityLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    private fun checkSavedFavoriteTeam(item: MenuItem) {
        teamDetailsViewModel.readFavoriteTeamEntity.observe(this, { favoriteTeamEntity ->
            for (savedTeam in favoriteTeamEntity) {
                if (savedTeam.teamId == teamId) {
                    changeMenuItemColor(item, R.color.yellow)
                    savedTeamId = savedTeam.id
                    teamSavedToFavorites = true
                }
            }
        })
    }

    private fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 6)
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}