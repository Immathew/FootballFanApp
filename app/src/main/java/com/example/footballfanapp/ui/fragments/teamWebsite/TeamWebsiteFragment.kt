package com.example.footballfanapp.ui.fragments.teamWebsite

import android.annotation.SuppressLint
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamWebsiteFragment : Fragment() {

    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private var _binding: FragmentTeamWebsiteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamDetailsViewModel =
            ViewModelProvider(requireActivity()).get(TeamDetailsViewModel::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamWebsiteBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.teamDetailsWebView.webViewClient = object : WebViewClient() {}
        binding.teamDetailsWebView.settings.javaScriptEnabled = true
        binding.teamDetailsWebView.settings.domStorageEnabled = true


        setApiData()

        return binding.root
    }

    private fun setApiData() {

        teamDetailsViewModel.teamDetailsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val websiteUrl = response.data!!.website.toString()
                    binding.teamDetailsWebView.loadUrl(websiteUrl)

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
                        context,
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