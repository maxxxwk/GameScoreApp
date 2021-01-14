package com.maxxxwk.gamescoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.maxxxwk.gamescoreapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.etFirstTeamName.setOnFocusChangeListener { v, hasFocus ->
            checkIsNameFieldEmpty(v as EditText, hasFocus)
        }
        binding.etSecondTeamName.setOnFocusChangeListener { v, hasFocus ->
            checkIsNameFieldEmpty(v as EditText, hasFocus)
        }
        binding.etMinutes.setOnFocusChangeListener { v, hasFocus ->
            checkIsTimeFieldEmpty(v as EditText, hasFocus)
        }
        binding.etSeconds.setOnFocusChangeListener { v, hasFocus ->
            checkIsTimeFieldEmpty(v as EditText, hasFocus)
        }
    }

    private fun checkIsNameFieldEmpty(editText: EditText, hasFocus: Boolean) {
        if (editText.text.isEmpty() && !hasFocus) {
            val warningMessage = when (editText.id) {
                R.id.etFirstTeamName -> getString(R.string.first_team_name_field_empty_warning)
                R.id.etSecondTeamName -> getString(R.string.second_team_name_field_empty_warning)
                else -> null
            }
            showWarning(warningMessage)
        }
    }

    private fun checkIsTimeFieldEmpty(editText: EditText, hasFocus: Boolean) {
        if (editText.text.isEmpty() && !hasFocus) {
            editText.setText("0")
        }
    }

    private fun showWarning(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}