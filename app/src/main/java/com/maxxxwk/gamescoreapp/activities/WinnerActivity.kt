package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxxxwk.gamescoreapp.databinding.ActivityWinnerBinding
import com.maxxxwk.gamescoreapp.models.Team
import java.lang.StringBuilder

class WinnerActivity : AppCompatActivity() {

    private val binding: ActivityWinnerBinding by lazy {
        ActivityWinnerBinding.inflate(
            layoutInflater
        )
    }

    companion object {
        private const val WINNER_KEY = "WINNER_KEY"
        private const val LOSER_KEY = "LOSER_KEY"

        fun start(context: Context, winner: Team, loser: Team) {
            val intent = Intent(context, WinnerActivity::class.java)
            intent.putExtra(WINNER_KEY, winner)
            intent.putExtra(LOSER_KEY, loser)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        putDataFromIntentToViews()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnNewGame.setOnClickListener {
            finish()
        }
        binding.btnShowWinnersList.setOnClickListener {
            showWinnersList()
        }
    }

    private fun showWinnersList() {
        WinnersListActivity.start(this)
        finish()
    }

    private fun putDataFromIntentToViews() {
        val winner = intent.getParcelableExtra<Team>(WINNER_KEY)
        val loser = intent.getParcelableExtra<Team>(LOSER_KEY)
        val gameScore = StringBuilder()
        winner?.let {
            binding.tvWinnerTeamName.text = it.name
            gameScore.append(it.score)
            gameScore.append(":")
        }
        loser?.let {
            binding.tvLoserTeamName.text = it.name
            gameScore.append(it.score)
            binding.tvGameScore.text = gameScore.toString()
        }
    }
}