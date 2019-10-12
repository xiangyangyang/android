package jp.co.solxyz.fleeksorm.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.java.KoinJavaComponent.inject

object UserPreferences {
    private val context: Context by inject(Application::class.java)
    private val preferences: SharedPreferences
        get() = context.getSharedPreferences("SormPreference", Context.MODE_PRIVATE)
    const val ONE_DAY_UNIXTIME = 12 * 60 * 60

    private const val SESSION_ID = "JSESSIONID"
    private const val USER_ID = "USER_ID"
    private const val USER_PASSWORD = "USER_PASSWORD"
    private const val LOGIN_USER_UID = "LOGIN_USER_UID"
    private const val LOGIN_USER_NAME = "LOGIN_USER_NAME"
    private const val LAST_LOGIN_TIME = "LAST_LOGIN_TIME"
    private const val LOGIN_USER_OID = "LOGIN_USER_OID"
    private const val USER_THUMBINAIL = "USER_THUMBINAIL"

    var sessionId: String
        get() = preferences.getString(SESSION_ID, "") ?: ""
        set(value) {
            preferences.edit().putString(SESSION_ID, "JSESSIONID=$value").apply()
        }
    var userOid : String
        get() = preferences.getString(LOGIN_USER_OID,"") ?: ""
        set(value) {
            preferences.edit().putString(LOGIN_USER_OID,value).apply()
        }

    var userLoginId: String
        get() = preferences.getString(USER_ID, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_ID, value).apply()
        }

    var userPwd: String
        get() = preferences.getString(USER_PASSWORD, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_PASSWORD, value).apply()
        }

    var loginUid: String
        get() = preferences.getString(LOGIN_USER_UID, "") ?: ""
        set(value) {
            preferences.edit().putString(LOGIN_USER_UID, value).apply()
        }

    var loginName: String
        get() = preferences.getString(LOGIN_USER_NAME, "") ?: ""
        set(value) {
            preferences.edit().putString(LOGIN_USER_NAME, value).apply()
        }

    var lastLoginTime: Long
        get() = preferences.getLong(
            LAST_LOGIN_TIME,
            (System.currentTimeMillis() / 1000).minus(ONE_DAY_UNIXTIME * 2)
        )
        set(value) {
            preferences.edit().putLong(LAST_LOGIN_TIME, value).apply()
        }

    var userThumbnail: String
        get() = preferences.getString(USER_THUMBINAIL,"") ?: ""
        set(value) {
            preferences.edit().putString(USER_THUMBINAIL,value).apply()
        }

    fun clearSesstion(){
        preferences.edit().putString(SESSION_ID, "").apply()
    }

    fun clearThumbinail(){
        preferences.edit().putString(USER_THUMBINAIL, "").apply()
    }
}