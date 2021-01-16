package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.callbacks.MessageDialogCallback
import com.maxxxwk.gamescoreapp.callbacks.TimerCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityScoreBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.ConfirmDialog
import com.maxxxwk.gamescoreapp.fragments.dialogs.MessageDialog
import com.maxxxwk.gamescoreapp.models.Winner
import com.maxxxwk.gamescoreapp.utils.GameTimer
import kotlin.math.max

class ScoreActivity : AppCompatActivity() {

    private val binding: ActivityScoreBinding by lazy { ActivityScoreBinding.inflate(layoutInflater) }
    private lateinit var gameTimer: GameTimer

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
        setTeamsNames()
        setupGameTimer()
        setupListeners()
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
                    onGameOver()
                }
            }
        )
    }

    private fun onGameOver() {
        val firstTeamScore = binding.tvFirstTeamScore.text.toString().toInt()
        val secondTeamScore = binding.tvSecondTeamScore.text.toString().toInt()
        val gameScore = "$firstTeamScore:$secondTeamScore"
        when {
            firstTeamScore != secondTeamScore -> {
                var winner = ""
                var loser = ""
                when {
                    firstTeamScore > secondTeamScore -> {
                        winner = binding.tvFirstTeamName.text.toString()
                        loser = binding.tvSecondTeamName.text.toString()

                    }
                    firstTeamScore < secondTeamScore -> {
                        winner = binding.tvSecondTeamName.text.toString()
                        loser = binding.tvFirstTeamName.text.toString()
                    }
                }
                WinnersListActivity.addNewWinner(
                    Winner(
                        winner,
                        max(firstTeamScore, secondTeamScore)
                    )
                )
                val message = "$winner is winner!"
                val callback = object : MessageDialogCallback {
                    override fun onConfirm() {
                        WinnerActivity.start(this@ScoreActivity, winner, loser, gameScore)
                        finish()
                    }
                }
                showGameOverDialog(message, callback)
            }
            else -> {
                val message = getString(R.string.draw_result_dialog_message)
                val callback = object : MessageDialogCallback {
                    override fun onConfirm() {
                        finish()
                    }
                }
                showGameOverDialog(message, callback)
            }
        }
    }

    private fun showGameOverDialog(
        message: String,
        callback: MessageDialogCallback
    ) {
        val title = getString(R.string.game_over_dialog_title)
        supportFragmentManager.beginTransaction()
            .add(MessageDialog.newInstance(title, message, callback), "TAG")
            .commitAllowingStateLoss()
    }

    private fun setupListeners() {
        binding.btnFirstTeamScoreIncrement.setOnClickListener {
            incrementScore(binding.tvFirstTeamScore)
        }
        binding.btnFirstTeamScoreDecrement.setOnClickListener {
            decrementScore(binding.tvFirstTeamScore)
        }
        binding.btnSecondTeamScoreIncrement.setOnClickListener {
            incrementScore(binding.tvSecondTeamScore)
        }
        binding.btnSecondTeamScoreDecrement.setOnClickListener {
            decrementScore(binding.tvSecondTeamScore)
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

    override fun onBackPressed() {
        showConfirmCancelDialog()
    }

    private fun setTeamsNames() {
        binding.tvFirstTeamName.text = intent.getStringExtra(FIRST_TEAM_NAME_KEY)
        binding.tvSecondTeamName.text = intent.getStringExtra(SECOND_TEAM_NAME_KEY)
    }

    private fun incrementScore(textView: TextView) {
        var score = textView.text.toString().toInt()
        if (score == Int.MAX_VALUE) {
            return
        }
        score += 1
        textView.text = score.toString()
    }

    private fun decrementScore(textView: TextView) {
        var score = textView.text.toString().toInt()
        if (score == 0) {
            return
        }
        score -= 1
        textView.text = score.toString()
    }

    private fun showConfirmCancelDialog() {
        val dialogTitle = getString(R.string.confirm_dialog_title)
        val dialogQuestion = getString(R.string.confirm_cancel_dialog_question)
        val dialogCallback = object : ConfirmDialogCallback {
            override fun onPositiveAnswer() {
                finish()
            }

            override fun onNegativeAnswer() {}
        }
        supportFragmentManager.beginTransaction()
            .add(ConfirmDialog.newInstance(dialogCallback, dialogTitle, dialogQuestion), "TAG")
            .commitAllowingStateLoss()
    }
}