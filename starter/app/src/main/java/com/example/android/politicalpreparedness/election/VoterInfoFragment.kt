package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding

    // Declare ViewModel
    private val viewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the VoterInfoViewModel after onViewCreated()"
        }

        ViewModelProvider(
            this,
            VoterInfoViewModel.Factory(activity.application)
        )[VoterInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        VoterInfoFragmentArgs.fromBundle(requireArguments()).let {
            viewModel.loadData(it.argElectionId, it.argDivision)
        }

        binding.errorBackButton.setOnClickListener { activity?.onBackPressed() }
        binding.votingUrl.setOnClickListener { viewModel.navigateToVote() }
        binding.ballotUrl.setOnClickListener { viewModel.navigateToBallots() }
        binding.followElectionButton.setOnClickListener { viewModel.followElection() }

        viewModel.navigationUrl.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty()) {
                viewModel.setNavigationHandled()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        viewModel.isFollowing.observe(viewLifecycleOwner) { isFollowing ->
            if (isFollowing) binding.followElectionButton.text =
                getString(R.string.unfollow_election)
            else binding.followElectionButton.text = getString(R.string.follow_election)
        }

        return binding.root
    }
}