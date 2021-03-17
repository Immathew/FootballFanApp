package com.example.footballfanapp.ui.fragments.teamSquad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.footballfanapp.databinding.FragmentTeamSquadBinding
import com.example.footballfanapp.models.TeamDetails
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TeamDetailsViewModel

class TeamSquadFragment : Fragment() {

    private lateinit var teamDetailsViewModel: TeamDetailsViewModel

    private var _binding: FragmentTeamSquadBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamDetailsViewModel =
            ViewModelProvider(requireActivity()).get(TeamDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamSquadBinding.inflate(inflater, container, false)


        setApiData()

        return binding.root
    }

    private fun setApiData() {

        teamDetailsViewModel.teamDetailsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val teamDetailsResponse = response.data!!

                    binding.test.text = teamDetailsResponse.toString()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        context,
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