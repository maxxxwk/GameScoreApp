package com.maxxxwk.gamescoreapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
        binding.etSeconds.addTextChangedListener(getSecondsLimitTextWatcher())
    }

    private fun hasEmptyField() = (binding.etFirstTeamName.text.isEmpty() ||
            binding.etSecondTeamName.text.isEmpty() ||
            binding.etMinutes.text.isEmpty() ||
            binding.etSeconds.text.isEmpty())

    private fun getFirstEmptyField() = when {
        binding.etFirstTeamName.text.isEmpty() -> binding.etFirstTeamName
        binding.etSecondTeamName.text.isEmpty() -> binding.etSecondTeamName
        binding.etMinutes.text.isEmpty() -> binding.etMinutes
        binding.etSeconds.text.isEmpty() -> binding.etSeconds
        else -> null
    }

    private fun setDefaultValueIfTimeFieldEmpty(editText: EditText, hasFocus: Boolean) {
        if (editText.text.isEmpty() && !hasFocus) {
            editText.setText(getString(R.string.time_field_default_value))
        }
    }

    private fun getSecondsLimitTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (s.toString() != "") {
                    val seconds = it.toString().toInt()
                    if (seconds >= 60) {
                        it.replace(0, it.length, (seconds - 60).toString())
                        if (binding.etMinutes.text.isEmpty()) {
                            binding.etMinutes.setText(getString(R.string.time_field_default_value))
                        }
                        val minutes = binding.etMinutes.text.toString().toInt()
                        binding.etMinutes.setText((minutes + 1).toString())
                    }
                }
            }
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
            .add(
                MessageDialog.newInstance(title, message, callback),
                getString(R.string.empty_field_error_dialog_tag)
            )
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