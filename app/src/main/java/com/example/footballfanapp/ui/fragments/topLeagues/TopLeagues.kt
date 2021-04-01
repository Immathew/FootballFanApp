package com.example.footballfanapp.ui.fragments.topLeagues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.viewModels.MainViewModel
import com.example.footballfanapp.adapters.TopLeaguesAdapter
import com.example.footballfanapp.databinding.FragmentTopLeaguesBinding
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TopLeaguesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopLeagues : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var topLeaguesViewModel: TopLeaguesViewModel

    private val mAdapter by lazy { TopLeaguesAdapter() }
    private var _binding: FragmentTopLeaguesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        topLeaguesViewModel = ViewModelProvider(requireActivity()).get(TopLeaguesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopLeaguesBinding.inflate(inflater, container, false)

        binding.topLeaguesRecyclerView.adapter = mAdapter
        binding.topLeaguesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        readDatabase()

        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readTopLeagues.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    val leagues = database[0].topLeaguesModel
                    val filteredLeagues = topLeaguesViewModel.removeUnwantedLeaguesFromDatabase(leagues)
                    mAdapter.setData(filteredLeagues)
                    binding.progressBar.visibility = View.INVISIBLE
                }
                else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {

        mainViewModel.getTopLeagues(topLeaguesViewModel.applyQuery())
        mainViewModel.topLeaguesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    val filteredLeagues = topLeaguesViewModel.removeUnwantedLeagues(response)

                    filteredLeagues.let {
                        mAdapter.setData(it) }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}