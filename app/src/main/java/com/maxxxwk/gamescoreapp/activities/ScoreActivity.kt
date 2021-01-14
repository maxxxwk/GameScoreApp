package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityScoreBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.ConfirmDialog

class ScoreActivity : AppCompatActivity() {

    private val binding: ActivityScoreBinding by lazy { ActivityScoreBinding.inflate(layoutInflater) }

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
        setupListeners()
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
    }

    override fun onBackPressed() {
        onCancel()
    }

    private fun setTimerInitialState() {
        val minutes = intent.getIntExtra(MINUTES_KEY, 0)
        val seconds = intent.getIntExtra(SECONDS_KEY, 0)
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
        val dialogTitle = getString(R.string.confirm_cancel_dialog_title)
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