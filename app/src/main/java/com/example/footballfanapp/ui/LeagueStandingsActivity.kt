package com.example.footballfanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.PagerAdapter
import com.example.footballfanapp.databinding.ActivityLeagueStandingsBinding
import com.example.footballfanapp.ui.fragments.leagueTable.LeagueTableFragment
import com.example.footballfanapp.ui.fragments.leagueTopScorers.LeagueTopScorersFragment
import com.example.footballfanapp.ui.fragments.leagueUpcomingMatches.LeagueUpcomingMatchesFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeagueStandingsActivity : AppCompatActivity() {

    private val args by navArgs<LeagueStandingsActivityArgs>()

    private lateinit var binding: ActivityLeagueStandingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeagueStandingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(LeagueTableFragment())
        fragments.add(LeagueUpcomingMatchesFragment())
        fragments.add(LeagueTopScorersFragment())

        val titles = ArrayList<String>()
        titles.add("Table")
        titles.add("Upcoming Matches")
        titles.add("Top Scorers")

        val leagueBundle = Bundle()
        leagueBundle.putInt("leagueId", args.competitionId)

        val pagerAdapter = PagerAdapter(
            leagueBundle,
            fragments,
            this
        )

        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) {tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}