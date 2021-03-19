package com.example.footballfanapp.ui.fragments.leagueUpcomingMatches

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
import com.example.footballfanapp.viewModels.LeagueStandingsViewModel
import com.example.footballfanapp.viewModels.LeagueUpcomingMatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeagueUpcomingMatchesFragment : Fragment() {

    private lateinit var leagueStandingsViewModel: LeagueStandingsViewModel
    private lateinit var leagueUpcomingMatchesViewModel: LeagueUpcomingMatchesViewModel

    private val mAdapter by lazy { LeagueUpcomingMatchesAdapter() }
    private var _binding: FragmentLeagueUpcomingMatchesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueStandingsViewModel =
            ViewModelProvider(requireActivity()).get(LeagueStandingsViewModel::class.java)
        leagueUpcomingMatchesViewModel =
            ViewModelProvider(requireActivity()).get(LeagueUpcomingMatchesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeagueUpcomingMatchesBinding.inflate(inflater, container, false)

        binding.leagueUpcomingMatchesRecyclerView.adapter = mAdapter
        binding.leagueUpcomingMatchesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val args = arguments
        val leagueIdFromMyBundle = args!!.getInt("leagueId")

        requestApiData(leagueIdFromMyBundle, leagueUpcomingMatchesViewModel.applyQueries())

        binding.scheduledChip.setOnClickListener {
            leagueUpcomingMatchesViewModel.setScheduledMatches()
            requestApiData(leagueIdFromMyBundle, leagueUpcomingMatchesViewModel.applyQueries())
        }
        binding.finishedChip.setOnClickListener {
            leagueUpcomingMatchesViewModel.setFinishedMatches()
            requestApiDataForFinishedMatches(
                leagueIdFromMyBundle,
                leagueUpcomingMatchesViewModel.applyQueries()
            )
        }
        return binding.root
    }

    private fun requestApiDataForFinishedMatches(leagueId: Int, queries: Map<String, String>) {

        leagueStandingsViewModel.getLeagueUpcomingMatches(leagueId, queries)

        leagueStandingsViewModel.leagueUpcomingMatchesResponse.observe(
            viewLifecycleOwner,
            { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        val sort = response.data!!.matches.sortedByDescending {
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

    private fun requestApiData(leagueId: Int, queries: Map<String, String>) {

        leagueStandingsViewModel.getLeagueUpcomingMatches(leagueId, queries)

        leagueStandingsViewModel.leagueUpcomingMatchesResponse.observe(
            viewLifecycleOwner,
            { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data!!.let {
                            mAdapter.setData(it)
                        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}