package com.maxxxwk.gamescoreapp.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.maxxxwk.gamescoreapp.callbacks.ConfirmCallback
import com.maxxxwk.gamescoreapp.databinding.MessageDialogFragmentBinding

class MessageDialog : DialogFragment() {
    private lateinit var binding: MessageDialogFragmentBinding
    private lateinit var confirmCallback: ConfirmCallback
    private lateinit var title: String
    private lateinit var message: String

    companion object {
        fun newInstance(title: String, message: String, callback: ConfirmCallback): MessageDialog {
            val instance = MessageDialog()
            instance.title = title
            instance.message = message
            instance.confirmCallback = callback
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MessageDialogFragmentBinding.inflate(layoutInflater, container, false)
        putDataInView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.btnConfirm.setOnClickListener {
            confirmCallback.onConfirm()
            dismiss()
        }
    }

    private fun putDataInView() {
        binding.tvDialogTitle.text = title
        binding.tvDialogMessage.text = message
    }
}