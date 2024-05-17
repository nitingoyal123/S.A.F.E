package com.example.safe

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.safe.databinding.MessageFakeOrNotLayoutBinding
import com.example.safe.model.Message

class MessageFakeOrNotDialogBox(message : Message, context: Context) : Dialog(context) {

    private var context = context
    private var message = message

    private lateinit var binding : MessageFakeOrNotLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(context as Activity, R.layout.message_fake_or_not_layout)


        var txt_fake_or_not = binding.txtScam
        var txt_sender = binding.txtSenderName
        var txt_message = binding.txtMessageBody
        var layout = binding.layout1

        txt_message.text = message.message
        txt_sender.text = message.phoneNumber

        if (message.DeepFake == true) {
            txt_fake_or_not.text = "LIKELY_SCAM"
            layout.setBackgroundColor(Color.argb(62, 235, 98, 98))
        }

    }
}