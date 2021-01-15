package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.callbacks.MessageDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityScoreBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.ConfirmDialog
import com.maxxxwk.gamescoreapp.fragments.dialogs.MessageDialog
import com.maxxxwk.gamescoreapp.models.Winner
import kotlin.math.max

class ScoreActivity : AppCompatActivity() {

    private val binding: ActivityScoreBinding by lazy { ActivityScoreBinding.inflate(layoutInflater) }
    private var minutes = 0
    private var seconds = 0
    private lateinit var countDownTimer: CountDownTimer

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
        setTimerInitialState()
        setupCountDownTimer()
        setupListeners()
    }

    private fun setupCountDownTimer() {
        val timeInMilliseconds = (minutes * 60 + seconds) * 1000L

        countDownTimer = object : CountDownTimer(timeInMilliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (seconds > 0) {
                    seconds -= 1
                } else {
                    seconds = 59
                    minutes -= 1
                }
                updateTimerState()
            }

            override fun onFinish() {
                onGameOver()
            }

        }
    }

    private fun onGameOver() {
        val firstTeamScore = binding.tvFirstTeamScore.text.toString().toInt()
        val secondTeamScore = binding.tvSecondTeamScore.text.toString().toInt()
        val gameScore = "$firstTeamScore:$secondTeamScore"
        val dialogTitle = getString(R.string.game_over_dialog_title)
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
                WinnersListActivity.addNewWinner(Winner(winner, max(firstTeamScore, secondTeamScore)))
                val message = "$winner is winner!"
                val callback = object : MessageDialogCallback {
                    override fun onConfirm() {
                        WinnerActivity.start(this@ScoreActivity, winner, loser, gameScore)
                        finish()
                    }
                }
                showGameOverDialog(dialogTitle, message, callback)
            }
            else -> {
                val message = getString(R.string.draw_result_dialog_message)
                val callback = object : MessageDialogCallback {
                    override fun onConfirm() {
                        finish()
                    }
                }
                showGameOverDialog(dialogTitle, message, callback)
            }
        }
    }
    private fun showGameOverDialog(title: String, message: String, callback: MessageDialogCallback) {
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
            onCancel()
        }
        binding.btnStartTimer.setOnClickListener {
            startTimer()
        }
        binding.btnStopTimer.setOnClickListener {
            stopTimer()
        }
    }

    override fun onBackPressed() {
        onCancel()
    }

    private fun startTimer() {
        if (minutes != 0 || seconds != 0) {
            countDownTimer.start()
        }
    }

    private fun stopTimer() {
        countDownTimer.cancel()
        setupCountDownTimer()
    }

    private fun setTimerInitialState() {
        minutes = intent.getIntExtra(MINUTES_KEY, 0)
        seconds = intent.getIntExtra(SECONDS_KEY, 0)
        updateTimerState()
    }

    private fun updateTimerState() {
        binding.tvTimer.text = when {
            minutes < 10 && seconds < 10 -> "0$minutes:0$seconds"
            minutes >= 10 && seconds < 10 -> "$minutes:0$seconds"
            minutes < 10 && seconds >= 10 -> "0$minutes:$seconds"
            else -> "$minutes:$seconds"
        }
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

    private fun onCancel() {
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