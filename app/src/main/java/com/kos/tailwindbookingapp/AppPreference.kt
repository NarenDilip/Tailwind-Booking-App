package com.kos.tailwindbookingapp

import android.content.Context
import android.content.SharedPreferences

import com.google.gson.Gson
import com.kos.tailwindbookingapp.model.LaneSession


/**
 * @since 27/2/17.
 * General preference works
 */

object AppPreference {

    private fun getEditor(c: Context?): SharedPreferences.Editor {
        return getPreference(c).edit()
    }

    private fun getPreference(c: Context?): SharedPreferences {
        return c!!.getSharedPreferences(
                Constants.PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Store the lane session data in stored preferences for further access. Consider like a session
     *
     * @param c Context of the app
     * @param u User Object
     */
    internal fun storeLaneSession(c: Context?, u: LaneSession?) {
        val e = getEditor(c)
        if (u == null) {
            e.remove("lane_session")
        } else {
            e.putString("lane_session", Gson().toJson(u))
        }
        e.apply()

    }

    /**
     *
     *
     * @param c Context of the app
     * @return Lane Session
     */
    fun getLaneSession(c: Context?): LaneSession {
        val s = getPreference(c).getString("lane_session", "")
        if (s == null || s.isEmpty()) {
            return LaneSession()
        }
        return Gson().fromJson(s, LaneSession::class.java)
    }



    /**
     * @param c     Context
     * @param key   key
     * @param value value
     */
    fun put(c: Context, key: String, value: String) {
        val editor = getEditor(c)
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * @param c   Context
     * @param key key
     * @param dv  default value
     */
    operator fun get(c: Context, key: String, dv: String): String? {
        return getPreference(c).getString(key, dv)
    }


    /**
     * @param c     Context
     * @param key   key
     * @param value value
     */
    fun put(c: Context, key: String, value: Int) {
        val editor = getEditor(c)
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * @param c   Context
     * @param key key
     * @param dv  default value
     */
    operator fun get(c: Context, key: String, dv: Int): Int {
        return getPreference(c).getInt(key, dv)
    }

    fun removeAll(c: Context) {
        val editor = getEditor(c)
        editor.clear()
        editor.apply()
    }

    fun clear(c: Context, key: String) {
        val editor = getEditor(c)
        editor.remove(key)
        editor.apply()
    }


}
