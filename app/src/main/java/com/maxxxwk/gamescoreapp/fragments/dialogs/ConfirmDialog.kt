package com.maxxxwk.gamescoreapp.fragments.dialogs

import android.icu.text.CaseMap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ConfirmDialogFragmentBinding

class ConfirmDialog: DialogFragment() {

    private lateinit var binding: ConfirmDialogFragmentBinding
    private lateinit var confirmDialogCallback: ConfirmDialogCallback
    private lateinit var title: String
    private lateinit var question: String

    companion object {
        fun newInstance(callback: ConfirmDialogCallback, title: String, question: String): ConfirmDialog {
            val instance = ConfirmDialog()
            instance.confirmDialogCallback = callback
            instance.title = title
            instance.question = question
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmDialogFragmentBinding.inflate(layoutInflater, container, false)
        putDataInView()
        setupListeners()
        return binding.root
    }

    private fun putDataInView() {
        binding.tvDialogTitle.text = title
        binding.tvDialogQuestion.text = question
    }

    private fun setupListeners() {
        binding.btnPositive.setOnClickListener {
            confirmDialogCallback.onPositiveAnswer()
            dismiss()
        }
        binding.btnNegative.setOnClickListener {
            confirmDialogCallback.onNegativeAnswer()
            dismiss()
        }
    }
}