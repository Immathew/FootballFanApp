package com.example.footballfanapp.ui.fragments.teamSquad

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballfanapp.adapters.TeamSquadAdapter
import com.example.footballfanapp.databinding.FragmentTeamSquadBinding
import com.example.footballfanapp.models.TeamDetails
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TeamDetailsViewModel

class TeamSquadFragment : Fragment() {

    private lateinit var teamDetailsViewModel: TeamDetailsViewModel

    private val mAdapter: TeamSquadAdapter by lazy { TeamSquadAdapter() }
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

        binding.teamSquadRecyclerView.adapter = mAdapter
        binding.teamSquadRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        setApiData()

        return binding.root
    }

    private fun setApiData() {

        teamDetailsViewModel.teamDetailsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val teamDetailsResponse = response.data!!

                    val teamWithoutCoach = teamDetailsResponse.squad.filterNot {
                        it.role == "COACH" || it.role == "ASSISTANT_COACH"
                    }

                    val newData = TeamDetails(
                        teamDetailsResponse.clubColors,
                        teamDetailsResponse.crestUrl,
                        teamDetailsResponse.founded,
                        teamDetailsResponse.id,
                        teamDetailsResponse.name,
                        teamWithoutCoach,
                        teamDetailsResponse.venue,
                        teamDetailsResponse.website
                    )

                    newData.let {
                        mAdapter.setData(it)
                    }

                    val findCoachName = teamDetailsResponse.squad.find {
                        it.role== "COACH"
                    }

                    binding.coachNameTv.text = findCoachName?.name

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