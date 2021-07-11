package com.example.bcd.FirebaseServices

import com.example.bcd.DataClass.NotificationData

data class PushNotification(
        val data: NotificationData,
        val to  :String
)