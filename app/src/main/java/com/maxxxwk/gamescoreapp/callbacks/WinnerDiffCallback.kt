package com.maxxxwk.gamescoreapp.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.maxxxwk.gamescoreapp.models.Team

class WinnerDiffCallback : DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
        return oldItem == newItem
    }
}