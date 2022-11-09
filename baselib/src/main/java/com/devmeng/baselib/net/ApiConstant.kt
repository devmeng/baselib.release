package com.devmeng.baselib.net

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  15:56
 * Version : 1
 */
interface ApiConstant {

    companion object {

        var BUILD_TYPE: Boolean = com.devmeng.baselib.BuildConfig.DEBUG

        var BASE_URL: String = when {
            BUILD_TYPE -> "https://www.wanandroid.com"
            else -> "https://www.wanandroid.com"
        }

    }

    object HomePage {
        //get 文章列表
        const val ARTICLE_LIST = "article/list/{PAGE}/json"

        //首页 Banner
        const val HOME_BANNER = "banner/json"

        //常用网络
        const val USUALLY_USE_NETWORK = "friend/json"

        //置顶文章
        const val STICK_TOP_ARTICLE = "article/top/json"
    }

    object UserOperation {
        /**
         * 参数
         * 文章 id:拼接在 url 上
         * title: 文章标题
         * link: 文章 url
         * author: 作者
         */
        const val COLLECT_ARTICLE =
            "lg/collect/user_article/update/{article_id}/json"

        const val CANCEL_COLLECT_ARTICLE =
            "lg/uncollect_originId/{article_id}/json"

        const val SIGN_IN = ""

    }

}