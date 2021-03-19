package com.example.footballfanapp.ui.fragments.teamMatches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.adapters.LeagueUpcomingMatchesAdapter
import com.example.footballfanapp.databinding.FragmentLeagueUpcomingMatchesBinding
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TeamDetailsViewModel
import com.example.footballfanapp.viewModels.TeamMatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamMatchesFragment : Fragment() {

    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private lateinit var teamMatchesViewModel: TeamMatchesViewModel

    private val mAdapter by lazy { LeagueUpcomingMatchesAdapter() }
    private var _binding: FragmentLeagueUpcomingMatchesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamDetailsViewModel =
            ViewModelProvider(requireActivity()).get(TeamDetailsViewModel::class.java)
        teamMatchesViewModel =
            ViewModelProvider(requireActivity()).get(TeamMatchesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // We are reusing layout and adapter which we already created in LeagueUpcomingMatches

        _binding = FragmentLeagueUpcomingMatchesBinding.inflate(layoutInflater, container, false)

        val args = arguments
        val teamIdFromBundle = args!!.getInt("teamId")

        binding.leagueUpcomingMatchesRecyclerView.adapter = mAdapter
        binding.leagueUpcomingMatchesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.scheduledChip.setOnClickListener {
            teamMatchesViewModel.setScheduledMatches()
            requestApiData(teamIdFromBundle, teamMatchesViewModel.applyQueries())
        }
        binding.finishedChip.setOnClickListener {
            teamMatchesViewModel.setFinishedMatches()
            requestApiDataForFinishedMatches(
                teamIdFromBundle,
                teamMatchesViewModel.applyQueries()
            )
        }

        requestApiData(teamIdFromBundle, teamMatchesViewModel.applyQueries())

        return binding.root
    }

    private fun requestApiDataForFinishedMatches(teamId: Int, queries: Map<String, String>) {

        teamDetailsViewModel.getTeamUpcomingMatches(teamId, queries)

        teamDetailsViewModel.teamUpcomingMatchesResponse.observe(
            viewLifecycleOwner,
            { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        val filter = teamMatchesViewModel.removeUnwantedLeagues(response)

                        val sort = filter.matches.sortedByDescending {
                            it.utcDate
                        }

                        mAdapter.setData(UpcomingMatchesModel(sort))

                        binding.leagueUpcomingMatchesRecyclerView.visibility = View.VISIBLE
                        binding.leagueUpcomingMatchesSadFaceImageView.visibility = View.INVISIBLE
                    }
                    is NetworkResult.Error -> {
                        binding.leagueUpcomingMatchesRecyclerView.visibility = View.INVISIBLE
                        binding.leagueUpcomingMatchesSadFaceImageView.visibility = View.VISIBLE

                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun requestApiData(teamId: Int, queries: Map<String, String>) {

        teamDetailsViewModel.getTeamUpcomingMatches(teamId, queries)

        teamDetailsViewModel.teamUpcomingMatchesResponse.observe(
            viewLifecycleOwner,
             { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        val filter = teamMatchesViewModel.removeUnwantedLeagues(response)

                        filter.let {
                            mAdapter.setData(it)
                        }

                        binding.leagueUpcomingMatchesRecyclerView.visibility = View.VISIBLE
                        binding.leagueUpcomingMatchesSadFaceImageView.visibility = View.INVISIBLE
                    }
                    is NetworkResult.Error -> {

                        binding.leagueUpcomingMatchesRecyclerView.visibility = View.INVISIBLE
                        binding.leagueUpcomingMatchesSadFaceImageView.visibility = View.VISIBLE

                        Toast.makeText(
                            context,
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

}