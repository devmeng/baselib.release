package com.devmeng.baselib.skin.utils

import android.content.Context
import android.content.SharedPreferences
import com.devmeng.baselib.utils.EMPTY

/**
 * Created by Richard
 * Version : 1
 * Description :
 * 使用 SharedPreference 存储皮肤包路径
 */
class SkinPreference private constructor() {

    private lateinit var context: Context
    private lateinit var sp: SharedPreferences

    companion object {

        const val SP_NAME_SKINS = "sp_skins"

        const val KEY_SKIN_PATH = "sp_skin_path"

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinPreference()
        }

        fun init(context: Context): SkinPreference {
            instance.context = context
            instance.sp = context.getSharedPreferences(SP_NAME_SKINS, Context.MODE_PRIVATE)
            return instance
        }

    }

    fun setSkin(skinPath: String = EMPTY) {
        sp.edit().putString(KEY_SKIN_PATH, skinPath).apply()
    }

    fun getSkin(): String {
        return sp.getString(KEY_SKIN_PATH, EMPTY).toString()
    }


}