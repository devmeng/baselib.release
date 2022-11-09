package com.devmeng.baselib.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * FileName: DateUtils
 * Author: 孟海粟
 * Date: 2021/6/8 17:35
 * Description: 时间与时间戳之间的相互转化
 */
class DateUtils {

    @SuppressLint("SimpleDateFormat")
    fun judgeChatInterval(oldMillis: Long, latestMillis: Long): Boolean {
        val millisOffset = latestMillis - oldMillis

        val format = SimpleDateFormat(TIME_ONLY_MINUTE)
        val date = Date(millisOffset)

        return format.format(date).toInt() >= 5
    }

    /**
     * 将时间戳转换成时间
     *
     * @param pattern 转换正则
     * @param millis  时间戳
     * @return
     */
    fun formatDate(pattern: String?, millis: Long): String {
        val format = SimpleDateFormat(pattern)
        val date = Date(millis)
        return format.format(date)
    }

    /**
     * 将时间转化成时间戳
     *
     * @param time    时间
     * @param pattern 时间正则
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun parseDate(time: String?, pattern: String?): Long {
        val format = SimpleDateFormat(pattern)
        var timeIndex: Long = 0
        try {
            val date = format.parse(time!!)
            timeIndex = date!!.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return timeIndex
    }

    fun formatTimeS(seconds: Long): String {
        var temp = 0
        val sb = StringBuffer()
        if (seconds > 3600) {
            temp = (seconds / 3600).toInt()
            sb.append(if (seconds / 3600 < 10) "0$temp:" else "$temp:")
            temp = (seconds % 3600 / 60).toInt()
            changeSeconds(seconds, temp, sb)
        } else {
            temp = (seconds % 3600 / 60).toInt()
            changeSeconds(seconds, temp, sb)
        }
        return sb.toString()
    }

    private fun changeSeconds(seconds: Long, temp: Int, sb: StringBuffer) {
        var temp = temp
        sb.append(if (temp < 10) "0$temp:" else "$temp:")
        temp = (seconds % 3600 % 60).toInt()
        sb.append(if (temp < 10) "0$temp" else "" + temp)
    }

    /**
     * 时间戳转成提示性日期格式（昨天、今天……)
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateToString(milSecond: Long, pattern: String?): String {
        val date = Date(milSecond)
        val format = SimpleDateFormat(pattern).format(date)
        val hintDate: String = judgeWhetherToday(milSecond)
        return if (TextUtils.isEmpty(hintDate)) {
            format
        } else {
            "$hintDate $format"
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun judgeWhetherToday(milSecond: Long): String {
        val date = Date(milSecond)
        var hintDate = EMPTY
        //获取日历
        val cal = Calendar.getInstance()
        //先获取年份
        val year = Integer.valueOf(SimpleDateFormat("yyyy").format(date))
        //获取一年中的第几天
        val day = Integer.valueOf(SimpleDateFormat("d").format(date))
        //获取当前年份 和 一年中的第几天
        val currentDate = Date(System.currentTimeMillis())
        val currentYear = Integer.valueOf(SimpleDateFormat("yyyy").format(currentDate))
        val currentDay = Integer.valueOf(SimpleDateFormat("d").format(currentDate))
        //年月日时间
        val fullDate = SimpleDateFormat(DATE_YYYY_MM_DD_MINUS_PATTERN).format(date)
        //计算 如果是去年的
        if (currentYear - year >= 1) {
            //如果当前正好是 1月1日 计算去年有多少天，指定时间是否是一年中的最后一天
            /*if (currentDay == 1) {
                val yearDay: Int = if (year % 400 == 0) {
                    366 //世纪闰年
                } else if (year % 4 == 0 && year % 100 != 0) {
                    366 //普通闰年
                } else {
                    365 //平年
                }
                if (day == yearDay) {
                    hintDate = "昨天"
                }
            }*/
            hintDate = fullDate
        } else if (currentYear - year == 0) {
            when {
                currentDay - day == 2 -> {
                    hintDate = "前天"
                }
                currentDay - day == 1 -> {
                    hintDate = "昨天"
                }
                currentDay - day > 2 -> {
                    cal.time = Date(milSecond)
                    hintDate = week(cal.get(Calendar.DAY_OF_WEEK))
                }
            }
            /*if (currentDay - day == 0) {
                hintDate = "今天"
            }*/
        }
        Logger.d("hint date -> $hintDate")
        return hintDate
    }

    fun week(tag: Int): String =
        when (tag) {
            1 -> {
                "星期日"
            }
            2 -> {
                "星期一"
            }
            3 -> {
                "星期二"
            }
            4 -> {
                "星期三"
            }
            5 -> {
                "星期四"
            }
            6 -> {
                "星期五"
            }
            else -> {
                "星期六"
            }
        }


    companion object {
        private var sDateUtils: DateUtils? = null
        const val ONLY_YEAR = "yyyy"
        const val ONLY_MONTH = "MM"
        const val ONLY_DAY = "dd"
        const val TIME_HH_MM_SS_12_PATTERN = "hh:mm:ss"
        const val TIME_HH_MM_SS_24_PATTERN = "HH:mm:ss"
        const val TIME_MM_SS_PATTERN = "mm:ss"
        const val TIME_ONLY_MINUTE = "mm"
        const val DATE_MM_DD_BIAS_PATTERN = "MM/dd"
        const val DATE_MM_DD_MINUS_PATTERN = "MM-dd"
        const val DATE_YYYY_MM_DD_BIAS_PATTERN = "yyyy/MM/dd"
        const val DATE_YYYY_MM_DD_MINUS_PATTERN = "yyyy-MM-dd"
        const val DATE_YYYY_MM_DD_CHARACTERS_PATTERN = "yyyy年MM月dd日"
        const val DATE_YYYY_MM_DD_TIME_BIAS_PATTERN = "yyyy/MM/dd HH:mm:ss"
        const val DATE_YYYY_MM_DD_TIME_MINUS_PATTERN = "yyyy-MM-dd HH:mm:ss"
        const val DATE_YYYY_MM_DD_TIME_CHARACTERS_PATTERN = "yyyy年MM月dd日 HH:mm:ss"
        val instance: DateUtils?
            get() {
                if (sDateUtils == null) {
                    sDateUtils = DateUtils()
                }
                return sDateUtils
            }
    }
}