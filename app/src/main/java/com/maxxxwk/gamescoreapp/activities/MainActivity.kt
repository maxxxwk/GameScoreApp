package com.maxxxwk.gamescoreapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.maxxxwk.gamescoreapp.R
import com.maxxxwk.gamescoreapp.callbacks.MessageDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ActivityMainBinding
import com.maxxxwk.gamescoreapp.fragments.dialogs.MessageDialog

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.etFirstTeamName.setOnFocusChangeListener { v, hasFocus ->
            showWarningIfNameFieldEmpty(v as EditText, hasFocus)
        }
        binding.etSecondTeamName.setOnFocusChangeListener { v, hasFocus ->
            showWarningIfNameFieldEmpty(v as EditText, hasFocus)
        }
        binding.etMinutes.setOnFocusChangeListener { v, hasFocus ->
            setDefaultValueIfTimeFieldEmpty(v as EditText, hasFocus)
        }
        binding.etSeconds.setOnFocusChangeListener { v, hasFocus ->
            setDefaultValueIfTimeFieldEmpty(v as EditText, hasFocus)
        }
        binding.btnConfirm.setOnClickListener {
            if (hasEmptyField()) {
                showEmptyFieldErrorMessageDialog()
            } else {
                startScoreActivity()
            }
        }
        binding.etFirstTeamName.addTextChangedListener(getTeamsNameTextWatcher())
        binding.etSecondTeamName.addTextChangedListener(getTeamsNameTextWatcher())
        binding.etSeconds.addTextChangedListener(getSecondsLimitTextWatcher())
    }

    private fun hasEmptyField(): Boolean {
        if (binding.etFirstTeamName.text.isEmpty() ||
            binding.etSecondTeamName.text.isEmpty() ||
            binding.etMinutes.text.isEmpty() ||
            binding.etSeconds.text.isEmpty()
        ) {
            return true
        }
        return false
    }

    private fun getFirstEmptyField() = when {
        binding.etFirstTeamName.text.isEmpty() -> binding.etFirstTeamName
        binding.etSecondTeamName.text.isEmpty() -> binding.etSecondTeamName
        binding.etMinutes.text.isEmpty() -> binding.etMinutes
        binding.etSeconds.text.isEmpty() -> binding.etSeconds
        else -> null
    }

    private fun showWarningIfNameFieldEmpty(editText: EditText, hasFocus: Boolean) {
        if (editText.text.isEmpty() && !hasFocus) {
            val warningMessage = when (editText.id) {
                R.id.etFirstTeamName -> getString(R.string.first_team_name_field_empty_warning)
                R.id.etSecondTeamName -> getString(R.string.second_team_name_field_empty_warning)
                else -> null
            }
            showWarning(warningMessage)
        }
    }

    private fun setDefaultValueIfTimeFieldEmpty(editText: EditText, hasFocus: Boolean) {
        if (editText.text.isEmpty() && !hasFocus) {
            editText.setText("0")
        }
    }

    private fun getTeamsNameTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length == 32) {
                showWarning(getString(R.string.teams_name_max_length_warning))
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun getSecondsLimitTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                val seconds = it.toString().toInt()
                if (seconds >= 60) {
                    binding.etSeconds.setText("${seconds - 60}")
                    if (binding.etMinutes.text.isEmpty()) {
                        binding.etMinutes.setText("1")
                    } else {
                        val minutes = binding.etMinutes.text.toString().toInt()
                        binding.etMinutes.setText("${minutes + 1}")
                    }
                }
            }
        }

    }

    private fun showWarning(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun showEmptyFieldErrorMessageDialog() {
        val title = getString(R.string.empty_field_error_dialog_title)
        val message = getString(R.string.empty_field_error_dialog_message)
        val callback = object : MessageDialogCallback {
            override fun onConfirm() {
                getFirstEmptyField()?.requestFocus()
            }
        }
        supportFragmentManager.beginTransaction()
            .add(MessageDialog.newInstance(title, message, callback), "TAG")
            .commitAllowingStateLoss()
    }

    private fun startScoreActivity() {
        val firstTeamName = binding.etFirstTeamName.text.toString()
        val secondTeamName = binding.etSecondTeamName.text.toString()
        val minutes = binding.etMinutes.text.toString().toInt()
        val seconds = binding.etSeconds.text.toString().toInt()
        ScoreActivity.start(this, firstTeamName, secondTeamName, minutes, seconds)
    }
}