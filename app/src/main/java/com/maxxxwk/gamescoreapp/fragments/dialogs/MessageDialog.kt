package com.maxxxwk.gamescoreapp.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.maxxxwk.gamescoreapp.callbacks.MessageDialogCallback
import com.maxxxwk.gamescoreapp.databinding.MessageDialogFragmentBinding

class MessageDialog : DialogFragment() {
    private lateinit var binding: MessageDialogFragmentBinding
    private lateinit var callback: MessageDialogCallback
    private lateinit var title: String
    private lateinit var message: String

    companion object {
        fun newInstance(title: String, message: String, callback: MessageDialogCallback) =
            MessageDialog().apply {
                this.title = title
                this.message = message
                this.callback = callback
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MessageDialogFragmentBinding.inflate(layoutInflater, container, false)
        putDataFromIntentToView()
        setupListeners()
        return binding.root
    }

    private fun putDataFromIntentToView() {
        binding.tvDialogTitle.text = title
        binding.tvDialogMessage.text = message
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            callback.onConfirm()
            dismiss()
        }
    }
}