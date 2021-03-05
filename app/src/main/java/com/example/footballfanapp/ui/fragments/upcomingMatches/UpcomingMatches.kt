package com.example.footballfanapp.ui.fragments.upcomingMatches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.viewModels.MainViewModel
import com.example.footballfanapp.adapters.UpcomingMatchesAdapter
import com.example.footballfanapp.databinding.FragmentUpcomingMatchesBinding
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_FROM
import com.example.footballfanapp.util.Constants.Companion.QUERY_DATE_TO
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.UpcomingMatchesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMatches : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var upcomingMatchesViewModel: UpcomingMatchesViewModel

    private val mAdapter by lazy { UpcomingMatchesAdapter() }
    private var _binding: FragmentUpcomingMatchesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        upcomingMatchesViewModel =
            ViewModelProvider(requireActivity()).get(UpcomingMatchesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingMatchesBinding.inflate(inflater, container, false)

        binding.upcomingMatchesRecyclerView.adapter = mAdapter
        binding.upcomingMatchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.YesterdayChip.setOnClickListener {
            upcomingMatchesViewModel.setYesterdayDate()
            requestApiData()
        }
        binding.todayChip.setOnClickListener {
            upcomingMatchesViewModel.setTodayDate()
            requestApiData()
        }
        binding.tomorrowChip.setOnClickListener {
            upcomingMatchesViewModel.setTomorrowDate()
            requestApiData()
        }

        requestApiData()

        return binding.root
    }

    private fun requestApiData() {

        mainViewModel.getUpcomingMatches(upcomingMatchesViewModel.applyQuery())
        mainViewModel.upcomingMatchesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val filteredMatches = upcomingMatchesViewModel.removeUnwantedLeagues(response)

                    filteredMatches.let {
                        mAdapter.setData(it)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



