package com.example.footballfanapp.ui.fragments.upcomingMatches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballfanapp.MainViewModel
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.UpcomingMatchesAdapter
import com.example.footballfanapp.databinding.FragmentUpcomingMatchesBinding
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMatches : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { UpcomingMatchesAdapter() }
    private var _binding: FragmentUpcomingMatchesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingMatchesBinding.inflate(inflater, container, false)

        binding.upcomingMatchesRecyclerView.adapter = mAdapter
        binding.upcomingMatchesRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        requestApiData()

        return binding.root
    }

    private fun requestApiData() {

        mainViewModel.getUpcomingMatches(applyQuery())
        mainViewModel.upcomingMatchesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    val filteredMatches = removeUnwantedLeagues(response)

                    filteredMatches.let {
                        mAdapter.setData(it) }
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

    private fun removeUnwantedLeagues(upcomingMatches: NetworkResult<UpcomingMatchesModel>): UpcomingMatchesModel {

        val filter = upcomingMatches.data?.matches?.filterNot {
            it.competition.code == "BSA" || it.competition.code == "CL" || it.competition.code =="EC"
                    || it.competition.code == "WC" || it.competition.code == "ELC"
        }
        val sortByLeague = filter?.sortedBy {
            it.competition.name
        }

        return UpcomingMatchesModel(sortByLeague!!)
    }

    private fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[""] = ""

        return queries
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}