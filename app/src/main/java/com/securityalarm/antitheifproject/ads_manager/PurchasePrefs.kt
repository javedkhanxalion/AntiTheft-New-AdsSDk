package com.securityalarm.antitheifproject.ads_manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

class PurchasePrefs(appContext: Context?) {
    var preferences: SharedPreferences

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    // Getters
    fun getInt(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    fun getFloatSpeed(key: String?): Float {
        return preferences.getFloat(key, 50.0F)
    }

    fun getFloatRadius(key: String?): Float {
        return preferences.getFloat(key, 1000.0F)
    }
//
//    fun getListInt(key: String?): ArrayList<Int> {
//        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
//        val arrayToList = ArrayList(Arrays.asList(*myList))
//        val newList = ArrayList<Int>()
//        for (item in arrayToList) newList.add(item.toInt())
//        return newList
//    }

    fun saveArrayList(list: ArrayList<String?>?, key: String?) {
        val editor: SharedPreferences.Editor = preferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun saveSwitches(key : String,switches: List<Boolean>) {
        val editor = preferences.edit()
        editor.putString(key, switches.joinToString(","))
        editor.apply()
    }

    fun loadSwitches(key : String): List<Boolean> {
        val switchesString = preferences.getString(key, null)
        return switchesString?.split(",")?.map { it.toBoolean() } ?: List(6) { true }
    }

    // Put methods
    fun putString(key: String?, value: String) {
        checkForNullKey(key)
        preferences.edit().putString(key, value).apply()
    }
    fun getString(key: String?): String? {
        return preferences.getString(key, "en")
    }

    fun getBoolean(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }
    fun getBooleanT(key: String?): Boolean {
        return preferences.getBoolean(key, true)
    }

    // Put methods
    fun putInt(key: String?, value: Int) {
        checkForNullKey(key)
        preferences.edit().putInt(key, value).apply()
    }
    // Put methods
    fun putFlot(key: String?, value: Float) {
        checkForNullKey(key)
        preferences.edit().putFloat(key, value).apply()
    }

    fun putLong(key: String?, value: Long) {
        checkForNullKey(key)
        preferences.edit().putLong(key, value).apply()
    }

    fun putBoolean(key: String?, value: Boolean) {
        checkForNullKey(key)
        preferences.edit().putBoolean(key, value).apply()
    }

    fun remove(key: String?) {
        preferences.edit().remove(key).apply()
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }
}