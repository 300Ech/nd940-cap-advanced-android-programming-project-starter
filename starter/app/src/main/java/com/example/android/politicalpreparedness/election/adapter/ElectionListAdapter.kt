package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class ElectionListAdapter(private val clickListener: ElectionListener, val headerTitle: String): ListAdapter<DataItem, RecyclerView.ViewHolder>(ElectionDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Election>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.ElectionItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val electionItem = getItem(position) as DataItem.ElectionItem
                holder.bind(clickListener, electionItem.election)
            }
            is TextViewHolder -> {
                holder.bind(headerTitle)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ElectionItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class TextViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(headerText: String) {
            view.findViewById<TextView>(R.id.headerTitle).text = headerText
        }

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

// Create ElectionViewHolder
class ViewHolder private constructor(val binding: ElectionListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: ElectionListener, item: Election) {
        binding.election = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    // Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = android.view.LayoutInflater.from(parent.context)
            val binding = ElectionListItemBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }
}

// Create ElectionDiffCallback
class ElectionDiffCallback: androidx.recyclerview.widget.DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

// Create ElectionListener
class ElectionListener(val clickListener: (election: Election) -> Unit) {
    fun onElectionClick(election: Election) = clickListener(election)
}

sealed class DataItem {
    data class ElectionItem(val election: Election): DataItem() {
        override val id = election.id
    }

    object Header: DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}