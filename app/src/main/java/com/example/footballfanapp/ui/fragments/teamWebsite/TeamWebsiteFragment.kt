package com.example.footballfanapp.ui.fragments.teamWebsite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.footballfanapp.databinding.FragmentTeamWebsiteBinding
import com.example.footballfanapp.util.NetworkResult
import com.example.footballfanapp.viewModels.TeamDetailsViewModel

class TeamWebsiteFragment : Fragment() {

    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private var _binding: FragmentTeamWebsiteBinding? = null
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
        _binding = FragmentTeamWebsiteBinding.inflate(layoutInflater, container, false)

        binding.teamDetailsWebView.webViewClient = object : WebViewClient() {}
        binding.teamDetailsWebView.settings.javaScriptEnabled = true

        setApiData()

        return binding.root
    }

    private fun setApiData() {

        teamDetailsViewModel.teamDetailsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val websiteUrl = response.data!!.website.toString()

                    binding.teamDetailsWebView.loadUrl(websiteUrl)
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