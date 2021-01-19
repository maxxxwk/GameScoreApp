package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.adapters.WinnersListAdapter
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityWinnersListBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.ConfirmDialog
import com.maxxxwk.gamescoreapp.models.Team

class WinnersListActivity : AppCompatActivity() {

    private val binding: ActivityWinnersListBinding by lazy {
        ActivityWinnersListBinding.inflate(
            layoutInflater
        )
    }


    companion object {
        private val listOfWinners = mutableListOf<Team>()
        private val adapter: WinnersListAdapter = WinnersListAdapter()

        fun addNewWinner(winner: Team) {
            listOfWinners.add(winner)
            listOfWinners.sortByDescending(Team::score)
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
            showListOfWinners()
        }
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnClear.setOnClickListener {
            showClearConfirmDialog()
        }
    }

    private fun showListOfWinners() {
        binding.tvEmptyWinnersListMessage.visibility = View.GONE
        binding.tvWinnersListTitle.visibility = View.VISIBLE
        binding.btnClear.visibility = View.VISIBLE
        binding.btnBack.visibility = View.VISIBLE
        binding.rvWinnersList.visibility = View.VISIBLE
        binding.rvWinnersList.layoutManager = LinearLayoutManager(this)
        binding.rvWinnersList.adapter = adapter
    }

    private fun hideListOfWinners() {
        binding.tvWinnersListTitle.visibility = View.GONE
        binding.btnClear.visibility = View.GONE
        binding.btnBack.visibility = View.GONE
        binding.rvWinnersList.visibility = View.GONE
        binding.tvEmptyWinnersListMessage.visibility = View.VISIBLE
    }

    private fun showClearConfirmDialog() {
        val title = getString(R.string.confirm_dialog_title)
        val question = getString(R.string.confirm_clear_dialog_message)
        val callback = object : ConfirmDialogCallback {
            override fun onPositiveAnswer() {
                clearWinnersList()
                hideListOfWinners()
            }

            override fun onNegativeAnswer() {}
        }
        supportFragmentManager.beginTransaction()
            .add(ConfirmDialog.newInstance(title, question, callback), "TAG")
            .commitAllowingStateLoss()
    }
}