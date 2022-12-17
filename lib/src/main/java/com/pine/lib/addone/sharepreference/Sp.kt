package com.pine.lib.addone.sharepreference

import android.content.SharedPreferences
import com.pine.lib.app.c

class SpHelper(fileName: String = "defaultSp") {

    private val prefs: SharedPreferences = c().getSharedPreferences(fileName, 0)

    fun get(key: String, default: String?): String? {
        return prefs.getString(key, default)
    }

    fun set(key: String, default: String?): String? {
        prefs.edit().putString(key, default).apply()
        return default
    }

    fun get(key: String, default: Int): Int {
        return prefs.getInt(key, default)
    }

    fun set(key: String, default: Int): Int {
        prefs.edit().putInt(key, default).apply()
        return default
    }

    fun get(key: String, default: Long): Long {
        return prefs.getLong(key, default)
    }

    fun set(key: String, default: Long): Long {
        prefs.edit().putLong(key, default).apply()
        return default
    }

    fun get(key: String, default: Float): Float {
        return prefs.getFloat(key, default)
    }

    fun set(key: String, default: Float): Float {
        prefs.edit().putFloat(key, default).apply()
        return default
    }

    fun get(key: String, default: Boolean): Boolean {
        return prefs.getBoolean(key, default)
    }

    fun set(key: String, default: Boolean): Boolean {
        prefs.edit().putBoolean(key, default).apply()
        return default
    }



}