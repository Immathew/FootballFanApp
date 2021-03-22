package com.example.footballfanapp.ui.fragments.upcomingMatches

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.R
import com.example.footballfanapp.viewModels.MainViewModel
import com.example.footballfanapp.adapters.UpcomingMatchesAdapter
import com.example.footballfanapp.databinding.FragmentUpcomingMatchesBinding
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.UpcomingMatchesViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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

        binding.lifecycleOwner = this
        binding.upcomingMatchesRecyclerView.adapter = mAdapter
        binding.upcomingMatchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        setChipsOnClickListeners()
        requestApiData()
        setDatesInChips()

        binding.upcomingMatchesHorizontalSV.post(Runnable {
            binding.upcomingMatchesHorizontalSV.scrollTo(
                220,
                0
            )
        })

        return binding.root
    }

    private fun setChipsOnClickListeners() {
        binding.dayBeforeYesterdayTwoChip.setOnClickListener {
            upcomingMatchesViewModel.setDayBeforeYesterdayTwoDate()
            requestApiData()
        }
        binding.dayBeforeYesterdayChip.setOnClickListener {
            upcomingMatchesViewModel.setDayBeforeYesterdayDate()
            requestApiData()
        }
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
        binding.dayAfterTomorrowChip.setOnClickListener {
            upcomingMatchesViewModel.setDayAfterTomorrowDate()
            requestApiData()
        }
        binding.dayAfterTomorrowTwoChip.setOnClickListener {
            upcomingMatchesViewModel.setDayAfterTomorrowTwoDate()
            requestApiData()
        }
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

                    if (filteredMatches.matches.isEmpty()) {
                        binding.upcomingMatchesRecyclerView.visibility = View.INVISIBLE
                        binding.upcomingMatchesSadFaceImageView.visibility = View.VISIBLE
                        binding.upcomingMatchesErrorTextView.visibility = View.VISIBLE
                        binding.upcomingMatchesErrorTextView.text =
                            getString(R.string.No_matches_today)
                    } else {
                        binding.upcomingMatchesRecyclerView.visibility = View.VISIBLE
                        binding.upcomingMatchesSadFaceImageView.visibility = View.INVISIBLE
                        binding.upcomingMatchesErrorTextView.visibility = View.INVISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    if (response.message.toString() == "No matches today") {
                        binding.upcomingMatchesRecyclerView.visibility = View.INVISIBLE
                        binding.upcomingMatchesSadFaceImageView.visibility = View.VISIBLE
                        binding.upcomingMatchesErrorTextView.visibility = View.VISIBLE
                        binding.upcomingMatchesErrorTextView.text =
                            getString(R.string.No_matches_today)
                    }
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

    @SuppressLint("SimpleDateFormat")
    private fun setDatesInChips() {
        val dateToday = Calendar.getInstance()

        dateToday.add(Calendar.DAY_OF_MONTH, -2)
        val dayBeforeYesterday = SimpleDateFormat("dd-MM").format(dateToday.time)
        binding.dayBeforeYesterdayChip.text = dayBeforeYesterday

        dateToday.add(Calendar.DAY_OF_MONTH, -1)
        val dayBeforeYesterdayTwo = SimpleDateFormat("dd-MM").format(dateToday.time)
        binding.dayBeforeYesterdayTwoChip.text = dayBeforeYesterdayTwo

        dateToday.add(Calendar.DAY_OF_MONTH, 5)
        val dayAfterTomorrow = SimpleDateFormat("dd-MM").format(dateToday.time)
        binding.dayAfterTomorrowChip.text = dayAfterTomorrow

        dateToday.add(Calendar.DAY_OF_MONTH, 1)
        val dayAfterTomorrowTwo = SimpleDateFormat("dd-MM").format(dateToday.time)
        binding.dayAfterTomorrowTwoChip.text = dayAfterTomorrowTwo
    }
}



