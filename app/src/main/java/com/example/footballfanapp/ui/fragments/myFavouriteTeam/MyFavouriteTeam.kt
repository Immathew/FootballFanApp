package com.example.footballfanapp.ui.fragments.myFavouriteTeam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.footballfanapp.R
import com.example.footballfanapp.adapters.MyFavoriteTeamAdapter
import com.example.footballfanapp.databinding.FragmentMyFavouriteTeamBinding
import com.example.footballfanapp.models.Team
import com.example.footballfanapp.viewModels.MainViewModel
import com.example.footballfanapp.viewModels.TeamDetailsViewModel


class MyFavouriteTeam : Fragment() {

    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private val mAdapter by lazy { MyFavoriteTeamAdapter(requireActivity(), teamDetailsViewModel) }
    private var _binding: FragmentMyFavouriteTeamBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamDetailsViewModel  = ViewModelProvider(requireActivity()).get(TeamDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyFavouriteTeamBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.myFavouriteTeamRecyclerView.adapter = mAdapter
        binding.myFavouriteTeamRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        readFavoriteTeamsFromDatabase()

        return binding.root
    }

    private fun readFavoriteTeamsFromDatabase() {
        teamDetailsViewModel.readFavoriteTeamEntity.observe(viewLifecycleOwner, { favoriteTeamEntity ->
            if(favoriteTeamEntity.isNotEmpty()) {
                favoriteTeamEntity.let {
                    mAdapter.setData(it)
                }
                binding.noFavoriteTeamsYetTextView.visibility = View.INVISIBLE
                binding.myFavouriteTeamRecyclerView.visibility = View.VISIBLE
            } else {
                binding.noFavoriteTeamsYetTextView.visibility = View.VISIBLE
                binding.myFavouriteTeamRecyclerView.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}