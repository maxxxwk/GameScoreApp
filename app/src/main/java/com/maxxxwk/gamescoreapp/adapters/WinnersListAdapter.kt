package com.maxxxwk.gamescoreapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxxxwk.gamescoreapp.callbacks.WinnerItemDiffCallback
import com.maxxxwk.gamescoreapp.databinding.WinnerListItemBinding
import com.maxxxwk.gamescoreapp.models.Team

class WinnersListAdapter :
    ListAdapter<Team, WinnersListAdapter.ViewHolder>(WinnerItemDiffCallback()) {

    class ViewHolder(private val binding: WinnerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(winner: Team) {
            binding.tvWinner.text = winner.name
            binding.tvScore.text = "Score: ${winner.score}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WinnerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}