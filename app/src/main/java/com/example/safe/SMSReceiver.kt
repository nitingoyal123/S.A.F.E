package com.example.safe

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.widget.RemoteViews
import androidx.compose.ui.graphics.Color
import com.example.safe.splashscreen.SplashScreenActivity
import com.example.safe.utility.AppUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SMSReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "sms_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null && Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                val bundle = intent.extras
                if (bundle != null) {
                    val pdus = bundle.get("pdus") as? Array<*> ?: return
                    val message = StringBuilder()
                    var sender = ""
                    for (pdu in pdus) {
                        val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                        sender = smsMessage.originatingAddress.toString()
                        message.append(smsMessage.messageBody)
                    }

                    val messageText = message.toString()
                    val otp = processMessage(messageText)

                    if (!otp.equals("-1")) {
                        showOtpNotification(context, sender, otp)
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            val isSpam = AppUtility.sendAPIRequest(messageText)
                            showNotification(context, sender, messageText, isSpam)
                        }
                    }
                }
            }
        }
    }

    // Function to extract OTP from the message
    fun extractOtp(message: String): String? {
        val otpPattern = "\\b\\d{4,6}\\b".toRegex()
        val matchResult = otpPattern.find(message)
        return matchResult?.value
    }

    // Function to check if the message contains OTP-related keywords and extract OTP
    fun processMessage(message: String): String {
        if (containsOtp(message)) {
            val otp = extractOtp(message)
            return otp ?: "-1" // If OTP is found, return it; otherwise return -1
        } else {
            return "-1" // Return -1 if no OTP-related keywords are found
        }
    }

    // Function to check if the message contains OTP-related keywords
    fun containsOtp(message: String): Boolean {
        val specialWords = listOf("otp", "password")
        val lowerMessage = message.lowercase()

        for (specialWord in specialWords) {
            if (lowerMessage.contains(specialWord)) {
                return true
            }
        }
        return false
    }


    private fun showOtpNotification(context: Context, sender: String, otp: String) {
        createNotificationChannel(context)

        val notificationIntent = Intent(context, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        // Set up the custom layout
        val customView = RemoteViews(context.packageName, R.layout.otp_notification_layout).apply {
            setTextViewText(R.id.phone_number, sender)  // Set the sender's phone number
            setTextViewText(R.id.otp_received, otp)     // Set the received OTP

        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24) // Use an appropriate icon for the notification
            .setCustomContentView(customView)             // Set the custom layout for the notification
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // Decorate with custom view style
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Set high priority for the notification
            .setContentIntent(pendingIntent)               // Add the pending intent
            .setAutoCancel(true)                           // Dismiss notification when clicked

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(NOTIFICATION_ID, builder.build())  // Push the notification
        }
    }


    private fun showNotification(context: Context, sender: String, message: String, isSpam: Boolean) {
        createNotificationChannel(context)

        val notificationIntent = Intent(context, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        // Set up the custom layout
        val customView = RemoteViews(context.packageName, R.layout.notification_otp).apply {
            setTextViewText(R.id.message_sender, sender)  // Set the sender's phone number
            setTextViewText(R.id.text_message, message) // Set the received OTP
            if (isSpam) {
                setInt(R.id.layout, "setBackgroundColor", android.graphics.Color.argb(100, 218, 0, 0)) // Spam - set red background
            } else {
                setInt(R.id.layout, "setBackgroundColor", android.graphics.Color.argb(86, 182, 245, 174)) // Not Spam - set green background
            }

        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.airtel)
            .setContentTitle("New SMS from $sender")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SMS Channel"
            val descriptionText = "Channel for SMS notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
