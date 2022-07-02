package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.toElectionOject
import com.google.android.material.snackbar.Snackbar

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionBinding
    private lateinit var adapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter
    private val navController by lazy { findNavController() }

    // Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the MainViewModel after onViewCreated()"
        }

        ViewModelProvider(
            this,
            ElectionsViewModel.Factory(activity.application)
        )[ElectionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Add binding values
        binding = FragmentElectionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ElectionListAdapter(ElectionListener { election ->
            //findNavController().navigate(MainFragmentDirections.actionShowDetail(election))
        }, resources.getString(R.string.upcoming_elections_recycler_view_title))

        savedElectionsAdapter = ElectionListAdapter(ElectionListener { savedElection ->
            //findNavController().navigate(MainFragmentDirections.actionShowDetail(election))
        }, resources.getString(R.string.saved_elections_recycler_view_title))

        binding.upcomingElectionsRecycler.adapter = adapter
        binding.savedElectionsRecycler.adapter = savedElectionsAdapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.upcomingElections.observe(viewLifecycleOwner) { elections ->
            adapter.addHeaderAndSubmitList(elections)
        }

        viewModel.savedElections.observe(viewLifecycleOwner) { savedElections ->
            val savedElectionsAsElections = savedElections.map {
                it.toElectionOject()
            }
            savedElectionsAdapter.addHeaderAndSubmitList(savedElectionsAsElections)
        }
    }

    //TODO: Link elections to voter info

    //TODO: Initiate recycler adapters

    //TODO: Populate recycler adapters
    //TODO: Refresh adapters when fragment loads

}