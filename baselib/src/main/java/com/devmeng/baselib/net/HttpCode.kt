package com.devmeng.baselib.net

import com.devmeng.baselib.utils.EMPTY


/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  20:32
 * Version : 1
 */
enum class HttpCode {

    SUCCESS(200, "请求成功"),

    FAILED(400, "请求失败"),

    SERVER_ERROR(500, "服务器异常"),

    NOT_FOUND(404, "网页走丢了");


    var inCode: Int = 0

    var inDesc: String = EMPTY

    constructor()

    constructor(code: Int, desc: String) {
        inCode = code
        inDesc = desc
    }

    open fun getDesc(code: Int): String {
        val desc: String = when (code) {
            SUCCESS.inCode -> {
                SUCCESS.inDesc
            }
            NOT_FOUND.inCode -> {
                NOT_FOUND.inDesc
            }
            SERVER_ERROR.inCode -> {
                SERVER_ERROR.inDesc
            }
            else -> {
                FAILED.inDesc
            }
        }
        return desc
    }


}

