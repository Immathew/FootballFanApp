package com.example.footballfanapp.ui.fragments.leagueTable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.adapters.LeagueTableAdapter
import com.example.footballfanapp.databinding.FragmentLeagueTableBinding
import com.example.footballfanapp.models.Standing
import com.example.footballfanapp.util.Constants.Companion.QUERY_STANDING_TYPE
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.LeagueStandingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeagueTableFragment : Fragment() {

    private lateinit var leagueStandingsViewModel: LeagueStandingsViewModel

    private val mAdapter by lazy { LeagueTableAdapter() }
    private var _binding: FragmentLeagueTableBinding? = null
    private val binding get() = _binding!!

    private var standingList: List<Standing>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueStandingsViewModel =
            ViewModelProvider(requireActivity()).get(LeagueStandingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeagueTableBinding.inflate(inflater, container, false)

        binding.leagueUpcomingMatchesRecyclerView.adapter = mAdapter
        binding.leagueUpcomingMatchesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val args = arguments
        val myBundle = args?.getInt("leagueId")

        requestApiData(myBundle!!)

        binding.standingTypeTotalChip.setOnClickListener {
            setStandingTypeTotal()
        }
        binding.standingTypeHomeChip.setOnClickListener {
            setStandingTypeHome()
        }
        binding.standingTypeAwayChip.setOnClickListener {
            setStandingTypeAway()
        }

        return binding.root
    }

    private fun requestApiData(leagueId: Int) {

        leagueStandingsViewModel.getLeagueStanding(leagueId)

        leagueStandingsViewModel.leagueStandingResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val leaguesStanding = response.data!!.standings
                    standingList = leaguesStanding

                    leaguesStanding.let {
                        mAdapter.setData(it[0])
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setStandingTypeAway() {
        standingList!!.let {
            mAdapter.setData(it[2])
        }
    }

    private fun setStandingTypeHome() {
        standingList!!.let {
            mAdapter.setData(it[1])
        }
    }

    private fun setStandingTypeTotal() {
        standingList!!.let {
            mAdapter.setData(it[0])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}