package com.devmeng.baselib.utils

import android.content.Context
import androidx.core.content.edit

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class SPUtils {

    companion object {

        private const val PREFERENCES_NAME: String = "config"

        fun getString(context: Context, key: String): String? {
            return getString(context, key, EMPTY)
        }

        fun getString(context: Context, key: String, defaultValue: String): String? {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return sp.getString(key, defaultValue)
        }

        fun getInt(context: Context, key: String): Int {
            return getInt(context, key, -1)
        }

        fun getInt(context: Context, key: String, defaultValue: Int): Int {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return sp.getInt(key, defaultValue)
        }

        fun getFloat(context: Context, key: String): Float {
            return getFloat(context, key, -1F)
        }

        fun getFloat(context: Context, key: String, defaultValue: Float): Float {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return sp.getFloat(key, defaultValue)
        }

        fun getLong(context: Context, key: String): Long {
            return getLong(context, key, -1L)
        }

        fun getLong(context: Context, key: String, defaultValue: Long): Long {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return sp.getLong(key, defaultValue)
        }

        fun getBoolean(context: Context, key: String): Boolean {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return sp.getBoolean(key, false)
        }

        fun getStringSet(context: Context, key: String): MutableSet<String>? {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            return sp.getStringSet(key, setOf())
        }

        fun putString(context: Context, key: String, value: String) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                putString(key, value)
            }
        }

        fun putInt(context: Context, key: String, value: Int) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                putInt(key, value)
            }
        }

        fun putFloat(context: Context, key: String, value: Float) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                putFloat(key, value)
            }
        }

        fun putLong(context: Context, key: String, value: Long) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                putLong(key, value)
            }
        }

        fun putBoolean(context: Context, key: String, value: Boolean) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                putBoolean(key, value)
            }
        }

        fun putStringSet(context: Context, key: String, value: MutableSet<String>) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                putStringSet(key, value)
            }
        }

        fun remove(context: Context, key: String) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                remove(key)
            }
        }

        fun clear(context: Context) {
            val sp =
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            sp.edit {
                clear()
            }
        }

    }

}