package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxxxwk.gamescoreapp.adapters.WinnersListAdapter
import com.maxxxwk.gamescoreapp.databinding.ActivityWinnersListBinding
import com.maxxxwk.gamescoreapp.models.Winner

class WinnersListActivity : AppCompatActivity() {

    private val binding: ActivityWinnersListBinding by lazy {
        ActivityWinnersListBinding.inflate(
            layoutInflater
        )
    }



    companion object {
        private val listOfWinners = mutableListOf<Winner>()
        private val adapter: WinnersListAdapter = WinnersListAdapter()

        fun addNewWinner(winner: Winner) {
            listOfWinners.add(winner)
            listOfWinners.sortByDescending(Winner::score)
            adapter.submitList(listOfWinners)
        }
        private fun clearWinnersList() {
            listOfWinners.clear()
            adapter.submitList(listOfWinners)
        }

        fun start(context: Context) {
            val intent = Intent(context, WinnersListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (listOfWinners.isNotEmpty()) {
            showRecyclerView()
        }
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnClear.setOnClickListener {
            clearWinnersList()
            hideRecyclerView()
        }
    }

    private fun showRecyclerView() {
        binding.tvEmptyWinnersListMessage.visibility = View.GONE
        binding.tvWinnersListTitle.visibility = View.VISIBLE
        binding.btnBack.visibility = View.VISIBLE
        binding.btnClear.visibility = View.VISIBLE
        binding.rvWinnersList.visibility = View.VISIBLE
        binding.rvWinnersList.layoutManager = LinearLayoutManager(this)
        binding.rvWinnersList.adapter = adapter
    }
    private fun hideRecyclerView() {
        binding.tvWinnersListTitle.visibility = View.GONE
        binding.btnBack.visibility = View.GONE
        binding.btnClear.visibility = View.GONE
        binding.rvWinnersList.visibility = View.GONE
        binding.tvEmptyWinnersListMessage.visibility = View.VISIBLE
    }
}