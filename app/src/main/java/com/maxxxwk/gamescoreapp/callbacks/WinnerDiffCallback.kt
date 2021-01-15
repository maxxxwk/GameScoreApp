package com.maxxxwk.gamescoreapp.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.maxxxwk.gamescoreapp.models.Winner

class WinnerDiffCallback : DiffUtil.ItemCallback<Winner>() {
    override fun areItemsTheSame(oldItem: Winner, newItem: Winner): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Winner, newItem: Winner): Boolean {
        return oldItem == newItem
    }
}