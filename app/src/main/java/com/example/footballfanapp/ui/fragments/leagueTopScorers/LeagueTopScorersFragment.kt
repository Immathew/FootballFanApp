package com.example.footballfanapp.ui.fragments.leagueTopScorers

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.adapters.LeagueTopScorersAdapter
import com.example.footballfanapp.databinding.FragmentLeagueTopScorersBinding
import com.example.footballfanapp.util.Constants.Companion.QUERY_LIMIT
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.LeagueStandingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeagueTopScorersFragment : Fragment() {

    private lateinit var leagueStandingsViewModel: LeagueStandingsViewModel

    private val mAdapter by lazy { LeagueTopScorersAdapter() }
    private var _binding: FragmentLeagueTopScorersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueStandingsViewModel =
            ViewModelProvider(requireActivity()).get(LeagueStandingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeagueTopScorersBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.leagueTopScorersRecyclerView.adapter = mAdapter
        binding.leagueTopScorersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val args = arguments
        val leagueIdFromMyBundle = args!!.getInt("leagueId")

        requestApiData(leagueIdFromMyBundle, applyQueries())

        return binding.root
    }

    private fun requestApiData(leagueId: Int, queries: Map<String, String>) {

        leagueStandingsViewModel.getLeagueTopScorers(leagueId, queries)

        leagueStandingsViewModel.leagueTopScorersResponse.observe(
            viewLifecycleOwner,
            { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data!!.let {
                            mAdapter.setData(it)
                        }
                        binding.sadFaceImageView.visibility = View.INVISIBLE
                        binding.errorTextView.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is NetworkResult.Error -> {
                        binding.sadFaceImageView.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.VISIBLE
                        binding.errorTextView.text = response.message.toString()
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            })
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_LIMIT] = "99"
        return queries
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}