package com.example.footballfanapp.ui.fragments.leagueTable

import android.os.Bundle
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

        binding.lifecycleOwner = this
        binding.leagueTableRecyclerView.adapter = mAdapter
        binding.leagueTableRecyclerView.layoutManager =
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

                    binding.sadFaceImageView.visibility = View.INVISIBLE
                    binding.errorTextView.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is NetworkResult.Error -> {
                    binding.sadFaceImageView.visibility = View.VISIBLE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorTextView.text = response.message.toString()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setStandingTypeAway() {
        if(standingList != null) {
            standingList!!.let {
                mAdapter.setData(it[2])
            }
        }
    }

    private fun setStandingTypeHome() {
        if(standingList != null) {
            standingList!!.let {
                mAdapter.setData(it[1])
            }
        }
    }

    private fun setStandingTypeTotal() {
        if(standingList != null) {
            standingList!!.let {
                mAdapter.setData(it[0])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}