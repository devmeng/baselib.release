package com.devmeng.baselib.base

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Richard -> MHS
 * Date : 2022/6/6  17:11
 * Version : 1
 */
abstract class BaseTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}