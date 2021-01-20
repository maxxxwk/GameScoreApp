package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.callbacks.MessageDialogCallback
import com.maxxxwk.gamescoreapp.callbacks.TimerCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityScoreBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.ConfirmDialog
import com.maxxxwk.gamescoreapp.fragments.dialogs.MessageDialog
import com.maxxxwk.gamescoreapp.models.Team
import com.maxxxwk.gamescoreapp.utils.GameResultChecker
import com.maxxxwk.gamescoreapp.utils.GameTimer

class ScoreActivity : AppCompatActivity() {

    private val binding: ActivityScoreBinding by lazy { ActivityScoreBinding.inflate(layoutInflater) }
    private lateinit var gameTimer: GameTimer
    private lateinit var firstTeam: Team
    private lateinit var secondTeam: Team

    companion object {
        private const val FIRST_TEAM_NAME_KEY = "FIRST_TEAM_NAME_KEY"
        private const val SECOND_TEAM_NAME_KEY = "SECOND_TEAM_NAME_KEY"
        private const val MINUTES_KEY = "MINUTES_KEY"
        private const val SECONDS_KEY = "SECONDS_KEY"

        fun start(
            context: Context,
            firstTeamName: String,
            secondTeamName: String,
            minutes: Int,
            seconds: Int
        ) {
            val intent = Intent(context, ScoreActivity::class.java)
            intent.putExtra(FIRST_TEAM_NAME_KEY, firstTeamName)
            intent.putExtra(SECOND_TEAM_NAME_KEY, secondTeamName)
            intent.putExtra(MINUTES_KEY, minutes)
            intent.putExtra(SECONDS_KEY, seconds)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initTeams()
        setupGameTimer()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnFirstTeamScoreIncrement.setOnClickListener {
            incrementScore(firstTeam)
        }
        binding.btnFirstTeamScoreDecrement.setOnClickListener {
            decrementScore(firstTeam)
        }
        binding.btnSecondTeamScoreIncrement.setOnClickListener {
            incrementScore(secondTeam)
        }
        binding.btnSecondTeamScoreDecrement.setOnClickListener {
            decrementScore(secondTeam)
        }
        binding.btnCancel.setOnClickListener {
            showConfirmCancelDialog()
        }
        binding.btnStartTimer.setOnClickListener {
            gameTimer.startTimer()
        }
        binding.btnStopTimer.setOnClickListener {
            gameTimer.stopTimer()
        }
    }

    private fun setupGameTimer() {
        val minutes = intent.getIntExtra(MINUTES_KEY, 0)
        val seconds = intent.getIntExtra(SECONDS_KEY, 0)
        gameTimer = GameTimer(
            minutes,
            seconds,
            binding.tvTimer,
            object : TimerCallback {
                override fun onFinish() {
                    onTimerFinish()
                }
            }
        )
    }

    private fun initTeams() {
        firstTeam = Team(
            intent.getStringExtra(FIRST_TEAM_NAME_KEY).toString(),
            0
        )
        secondTeam = Team(
            intent.getStringExtra(SECOND_TEAM_NAME_KEY).toString(),
            0
        )
        binding.tvFirstTeamName.text = firstTeam.name
        binding.tvFirstTeamScore.text = firstTeam.score.toString()
        binding.tvSecondTeamName.text = secondTeam.name
        binding.tvSecondTeamScore.text = secondTeam.score.toString()
    }

    private fun incrementScore(team: Team) {
        if (team.score != Int.MAX_VALUE) {
            team.score += 1
            updateScore(team)
        }
    }

    private fun decrementScore(team: Team) {
        if (team.score != 0) {
            team.score -= 1
            updateScore(team)
        }
    }

    private fun updateScore(team: Team) {
        when (team.name) {
            binding.tvFirstTeamName.text.toString() -> {
                binding.tvFirstTeamScore.text = team.score.toString()
            }
            else -> {
                binding.tvSecondTeamScore.text = team.score.toString()
            }
        }
    }

    private fun onTimerFinish() {
        GameResultChecker(firstTeam, secondTeam).apply {
            when {
                isDraw -> {
                    val message = getString(R.string.draw_result_dialog_message)
                    val callback = object : MessageDialogCallback {
                        override fun onConfirm() {
                            finish()
                        }
                    }
                    showGameResultDialog(message, callback)
                }
                else -> {
                    winner?.let { winner ->
                        WinnersListActivity.addNewWinner(winner)
                        val message = "${winner.name} is winner!"
                        loser?.let { loser ->
                            val callback = object : MessageDialogCallback {
                                override fun onConfirm() {
                                    WinnerActivity.start(this@ScoreActivity, winner, loser)
                                    finish()
                                }
                            }
                            showGameResultDialog(message, callback)
                        }
                    }
                }
            }
        }
    }

    private fun showGameResultDialog(
        message: String,
        callback: MessageDialogCallback
    ) {
        val title = getString(R.string.time_over_dialog_title)
        supportFragmentManager.beginTransaction()
            .add(MessageDialog.newInstance(title, message, callback), "TAG")
            .commitAllowingStateLoss()
    }

    private fun showConfirmCancelDialog() {
        val title = getString(R.string.confirm_dialog_title)
        val question = getString(R.string.confirm_cancel_dialog_question)
        val callback = object : ConfirmDialogCallback {
            override fun onPositiveAnswer() {
                finish()
            }

            override fun onNegativeAnswer() {}
        }
        supportFragmentManager.beginTransaction()
            .add(ConfirmDialog.newInstance(title, question, callback), "TAG")
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        showConfirmCancelDialog()
    }
}