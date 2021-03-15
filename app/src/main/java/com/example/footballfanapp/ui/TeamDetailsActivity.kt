package com.example.footballfanapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.PagerAdapter
import com.example.footballfanapp.databinding.ActivityTeamDeteailsBinding
import com.example.footballfanapp.ui.fragments.teamMatches.TeamMatchesFragment
import com.example.footballfanapp.ui.fragments.teamSquad.TeamSquadFragment
import com.example.footballfanapp.ui.fragments.teamWebsite.TeamWebsiteFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamDetailsActivity : AppCompatActivity() {



    private lateinit var binding: ActivityTeamDeteailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//        teamDetailsBundle.putInt("teamId", args.teamId)

        val pagerAdapter = PagerAdapter(
            teamDetailsBundle,
            fragments,
            this
        )

        binding.teamDetailsViewPager2.apply {
            adapter = pagerAdapter
        }

//        binding.teamDetailsViewPager2.adapter = pagerAdapter

        TabLayoutMediator(
            binding.teamDetailsTabLayout,
            binding.teamDetailsViewPager2
        ) { tab, position ->
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