package com.maxxxwk.gamescoreapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityWinnerBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.ConfirmDialog
import com.maxxxwk.gamescoreapp.models.Team
import java.lang.StringBuilder

class WinnerActivity : AppCompatActivity() {

    private val binding: ActivityWinnerBinding by lazy {
        ActivityWinnerBinding.inflate(
            layoutInflater
        )
    }

    private var winner: Team? = null
    private var loser: Team? = null

    companion object {
        private const val WINNER_KEY = "WINNER_KEY"
        private const val LOSER_KEY = "LOSER_KEY"

        fun start(context: Context, winner: Team, loser: Team) {
            context.startActivity(Intent(context, WinnerActivity::class.java).apply {
                putExtra(WINNER_KEY, winner)
                putExtra(LOSER_KEY, loser)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pullDataFromIntent()
        updateViews()
        setupListeners()
    }

    private fun pullDataFromIntent() {
        winner = intent.getParcelableExtra(WINNER_KEY)
        loser = intent.getParcelableExtra(LOSER_KEY)
    }

    private fun updateViews() {
        val gameScore = StringBuilder()
        winner?.let {
            binding.tvWinnerTeamName.text = it.name
            gameScore.append(it.score)
            gameScore.append(getString(R.string.score_delimiter))
        }
        loser?.let {
            binding.tvLoserTeamName.text = it.name
            gameScore.append(it.score)
            binding.tvGameScore.text = gameScore.toString()
        }
    }

    private fun setupListeners() {
        binding.btnNewGame.setOnClickListener {
            finish()
        }
        binding.btnShowWinnersList.setOnClickListener {
            showWinnersList()
        }
        binding.btnShare.setOnClickListener {
            showShareConfirmDialog()
        }
    }

    private fun showWinnersList() {
        WinnersListActivity.start(this)
        finish()
    }

    private fun showShareConfirmDialog() {
        val title = getString(R.string.confirm_dialog_title)
        val question = getString(R.string.share_result_confirm_dialog_question)
        val callback = object : ConfirmDialogCallback {
            override fun onPositiveAnswer() {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT, getString(
                            R.string.share_result_text,
                            winner?.name,
                            loser?.name,
                            winner?.score,
                            loser?.score
                        )
                    )
                }
                startActivity(
                    Intent.createChooser(
                        intent,
                        getString(R.string.share_intent_chooser_text)
                    )
                )
            }

            override fun onNegativeAnswer() {}
        }
        supportFragmentManager.beginTransaction()
            .add(
                ConfirmDialog.newInstance(title, question, callback),
                getString(R.string.share_result_confirm_dialog_tag)
            )
            .commitAllowingStateLoss()
    }
}