package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxxxwk.gamescoreapp.databinding.ActivityWinnerBinding

class WinnerActivity : AppCompatActivity() {

    private val binding: ActivityWinnerBinding by lazy {
        ActivityWinnerBinding.inflate(
            layoutInflater
        )
    }

    companion object {
        private const val WINNER_NAME_KEY = "WINNER_NAME_KEY"
        private const val LOSER_NAME_KEY = "LOSER_NAME_KEY"
        private const val GAME_SCORE_KEY = "GAME_SCORE_KEY"

        fun start(context: Context, winner: String, loser: String, gameScore: String) {
            val intent = Intent(context, WinnerActivity::class.java)
            intent.putExtra(WINNER_NAME_KEY, winner)
            intent.putExtra(LOSER_NAME_KEY, loser)
            intent.putExtra(GAME_SCORE_KEY, gameScore)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        putDataInViews()
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

    fun putDataInViews() {
        val winner = intent.getStringExtra(WINNER_NAME_KEY)
        val loser = intent.getStringExtra(LOSER_NAME_KEY)
        val gameScore = intent.getStringExtra(GAME_SCORE_KEY)
        binding.tvWinnerTeamName.text = winner
        binding.tvLoserTeamName.text = loser
        binding.tvGameScore.text = gameScore
    }
}