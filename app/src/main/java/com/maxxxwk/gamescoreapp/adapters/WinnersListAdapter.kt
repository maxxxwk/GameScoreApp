package com.maxxxwk.gamescoreapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxxxwk.gamescoreapp.callbacks.WinnerDiffCallback
import com.maxxxwk.gamescoreapp.databinding.WinnerListItemBinding
import com.maxxxwk.gamescoreapp.models.Winner

class WinnersListAdapter :
    ListAdapter<Winner, WinnersListAdapter.ViewHolder>(WinnerDiffCallback()) {

    class ViewHolder(private val binding: WinnerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(winner: Winner) {
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