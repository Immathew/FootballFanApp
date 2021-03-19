package com.example.footballfanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.PagerAdapter
import com.example.footballfanapp.databinding.ActivityTeamDeteailsBinding
import com.example.footballfanapp.models.TeamDetails
import com.example.footballfanapp.ui.fragments.teamMatches.TeamMatchesFragment
import com.example.footballfanapp.ui.fragments.teamSquad.TeamSquadFragment
import com.example.footballfanapp.ui.fragments.teamWebsite.TeamWebsiteFragment
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TeamDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamDetailsActivity : AppCompatActivity() {

    private val teamDetailsViewModel by viewModels<TeamDetailsViewModel> {defaultViewModelProviderFactory}

    private lateinit var binding: ActivityTeamDeteailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val teamId = intent.getIntExtra("teamId", 0)
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

    private fun requestApiData(teamId: Int) {
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
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop*6)       // "8" was obtained experimentally
    }
}