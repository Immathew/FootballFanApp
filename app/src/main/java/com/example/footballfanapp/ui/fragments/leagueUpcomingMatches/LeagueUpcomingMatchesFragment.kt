package com.example.footballfanapp.ui.fragments.leagueUpcomingMatches

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.LeagueUpcomingMatchesAdapter
import com.example.footballfanapp.databinding.FragmentLeagueUpcomingMatchesBinding
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_FROM
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_TO
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.LeagueStandingsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class LeagueUpcomingMatchesFragment : Fragment() {

    private lateinit var leagueStandingsViewModel: LeagueStandingsViewModel

    private val mAdapter by lazy { LeagueUpcomingMatchesAdapter() }
    private var _binding: FragmentLeagueUpcomingMatchesBinding? = null
    private val binding get() = _binding!!

    private var status = "SCHEDULED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueStandingsViewModel =
            ViewModelProvider(requireActivity()).get(LeagueStandingsViewModel::class.java)
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

        setScheduledMatches()
        requestApiData(leagueIdFromMyBundle, applyQueries())

        binding.scheduledChip.setOnClickListener {
            setScheduledMatches()
            requestApiData(leagueIdFromMyBundle, applyQueries())
        }
        binding.finishedChip.setOnClickListener {
            setFinishedMatches()
            requestApiData(leagueIdFromMyBundle, applyQueries())
        }


        return binding.root
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

    fun applyQueries(): HashMap<String, String> {


        val queries: HashMap<String, String> = HashMap()

        queries["status"] = status

        return queries
    }

    @SuppressLint("SimpleDateFormat")
    fun setScheduledMatches() {
        status = "SCHEDULED"

    }

    fun setFinishedMatches() {
        status = "FINISHED"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}