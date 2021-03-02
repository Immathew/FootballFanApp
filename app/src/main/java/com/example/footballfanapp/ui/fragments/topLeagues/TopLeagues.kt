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
import com.example.footballfanapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopLeagues : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private val mAdapter by lazy { TopLeaguesAdapter() }
    private lateinit var binding: FragmentTopLeaguesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopLeaguesBinding.inflate(inflater, container, false)

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
                    response.data?.let { mAdapter.setData(it) }
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

    private fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries["plan"] = "TIER_ONE"

        return queries
    }
}