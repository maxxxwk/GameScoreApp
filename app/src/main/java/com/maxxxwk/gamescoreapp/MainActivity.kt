package com.maxxxwk.gamescoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.maxxxwk.gamescoreapp.callbacks.ConfirmCallback
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
                val title = getString(R.string.empty_field_error_title)
                val message = getString(R.string.empty_field_error_message)
                val callback = object : ConfirmCallback {
                    override fun onConfirm() {
                        getFirstEmptyField()?.requestFocus()
                    }
                }
                showMessageDialog(title, message, callback)
            }
        }
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

    private fun showWarning(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun showMessageDialog(title: String, message: String, callback: ConfirmCallback) {
        supportFragmentManager.beginTransaction()
            .add(MessageDialog.newInstance(title, message, callback), "TAG")
            .commitAllowingStateLoss()
    }
}