package com.example.android.politicalpreparedness.election.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android.politicalpreparedness.databinding.ElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    //TODO: Add companion object to inflate ViewHolder (from)
}

//TODO: Create ElectionViewHolder
class ElectionViewHolder private constructor(val binding: ElectionListItemBinding): androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ElectionViewHolder {
            val layoutInflater = android.view.LayoutInflater.from(parent.context)
            val binding = ElectionListItemBinding.inflate(layoutInflater, parent, false)
            return ElectionViewHolder(binding)
        }
    }
}

//TODO: Create ElectionDiffCallback
class ElectionDiffCallback: androidx.recyclerview.widget.DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

//TODO: Create ElectionListener
interface ElectionListener {
    fun onElectionClick(election: Election)
}