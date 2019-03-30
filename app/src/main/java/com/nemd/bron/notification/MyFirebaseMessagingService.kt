package com.nemd.bron.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nemd.bron.R
import com.nemd.bron.SharedPreferenceHelper
import com.nemd.bron.hcp.HcpMainActivity
import com.nemd.bron.hcp.HcpRequestDataActivity
import com.nemd.bron.patient.PendingRequestActivity
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("From: ${remoteMessage?.from}")

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Timber.d("Message Notification Body: ${it.body}")
        }

        remoteMessage?.data?.let { data ->
            val pendingIntent =
                if (data.containsKey("requestId")) {
                    val intent = Intent(this, PendingRequestActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra(PendingRequestActivity.REQUEST_ID_EXTRA, data["requestId"] as String)
                    }

                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
                else if (data.containsKey("requestApprovedId")) {
                    val intent = Intent(this, HcpMainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        putExtra(HcpMainActivity.REQUEST_ID_EXTRA, data["requestApprovedId"] as String)
                    }

                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
                else {
                    PendingIntent.getActivity(this, 0, null, PendingIntent.FLAG_UPDATE_CURRENT)
                }


            val builder = NotificationCompat.Builder(this, createNotificationChannel())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(SharedPreferenceHelper.getNotificationId(this@MyFirebaseMessagingService), builder.build())
            }
        }
    }

    private fun createNotificationChannel(): String {
        val channelId = "ConsentChannel"
        val channelName = getString(R.string.consent_channel_name)
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Timber.d("Refreshed token: $token")

        if (token != null) {

            FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->

                val userUpdate = HashMap<String, Any>()
                userUpdate["firebase_token"] = token

                FirebaseFirestore.getInstance().collection("users").document(userId)
                    .set(userUpdate, SetOptions.merge())
                    .addOnSuccessListener { Timber.d("DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Timber.e(e, "Error writing document") }
            }
        }
    }
}