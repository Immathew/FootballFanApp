package com.example.footballfanapp.ui.fragments.topLeagues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.MainViewModel
import com.example.footballfanapp.adapters.TopLeaguesAdapter
import com.example.footballfanapp.databinding.FragmentTopLeaguesBinding
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopLeagues : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { TopLeaguesAdapter() }
    private var _binding: FragmentTopLeaguesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopLeaguesBinding.inflate(inflater, container, false)

        binding.topLeaguesRecyclerView.adapter = mAdapter
        binding.topLeaguesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        requestApiData()




        return binding.root
    }

    private fun requestApiData() {

        mainViewModel.getTopLeagues(applyQuery())
        mainViewModel.topLeaguesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    val filteredLeagues = removeUnwantedLeagues(response)

                    filteredLeagues.let {
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

    private fun removeUnwantedLeagues(topLeague: NetworkResult<TopLeaguesModel>): TopLeaguesModel {
        val filter = topLeague.data?.competitions?.filterNot {
            it.code == "BSA" || it.code == "CL" || it.code =="EC"
                    || it.code == "WC" || it.code == "ELC"
        }
        val filter2 = topLeague.data?.competitions?.groupBy {
            it.code
        }

        return TopLeaguesModel(filter!!)
    }

    private fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries["plan"] = "TIER_ONE"

        return queries
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}