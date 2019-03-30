package com.nemd.bron

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceHelper {

    private const val SHARED_PREFS_NAME = "SHARED_PREFS_NAME"

    fun getSharedPreferences(context: Context) : SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    private const val USER_TYPE_PREF = "USER_TYPE_PREF"

    fun getUserType(context: Context) : UserType? {
        val userTypeName = getSharedPreferences(context).getString(USER_TYPE_PREF, null)

        userTypeName?.let {
            return UserType.valueOf(it)
        }

        return null
    }


    fun setUserType(context: Context, userType: UserType) {
        getSharedPreferences(context).edit().putString(USER_TYPE_PREF, userType.name).apply()
    }

    private const val NOTIFICATION_ID_PREF = "NOTIFICATION_ID_PREF"

    fun getNotificationId(context: Context): Int {
        val notificationId = getSharedPreferences(context).getInt(NOTIFICATION_ID_PREF, 0) + 1

        getSharedPreferences(context).edit().putInt(NOTIFICATION_ID_PREF, notificationId).apply()

        return notificationId
    }

}

enum class UserType {
    PATIENT, HCP
}
