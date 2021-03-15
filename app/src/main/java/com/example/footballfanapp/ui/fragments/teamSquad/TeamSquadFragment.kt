package com.example.footballfanapp.ui.fragments.teamSquad

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.footballfanapp.databinding.FragmentTeamSquadBinding

class TeamSquadFragment : Fragment() {

    private var _binding: FragmentTeamSquadBinding? = null
    private val binding get()= _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamSquadBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle = args?.getInt("teamId")

        binding.test.text = myBundle.toString()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}