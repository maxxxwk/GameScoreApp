package com.maxxxwk.gamescoreapp.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.maxxxwk.gamescoreapp.callbacks.ConfirmDialogCallback
import com.maxxxwk.gamescoreapp.databinding.ConfirmDialogFragmentBinding

class ConfirmDialog : DialogFragment() {

    private lateinit var binding: ConfirmDialogFragmentBinding
    private lateinit var confirmDialogCallback: ConfirmDialogCallback
    private lateinit var title: String
    private lateinit var question: String

    companion object {
        fun newInstance(
            title: String,
            question: String,
            callback: ConfirmDialogCallback
        ) = ConfirmDialog().apply {
            this.confirmDialogCallback = callback
            this.title = title
            this.question = question
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmDialogFragmentBinding.inflate(layoutInflater, container, false)
        putDataFromIntentToView()
        setupListeners()
        return binding.root
    }

    private fun putDataFromIntentToView() {
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