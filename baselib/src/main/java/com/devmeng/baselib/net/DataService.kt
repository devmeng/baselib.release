package com.devmeng.baselib.net

import com.devmeng.baselib.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  11:37
 * Version : 1
 */
class DataService private constructor() {

    companion object {

        var duration: Long = 10_000

        val instance: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Retrofit.Builder()
                .baseUrl(ApiConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()
                .create()
        }

        private fun getOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
            return OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .addInterceptor(Interceptor { chain ->
                    chain.proceed(getRequest(chain.request()))
                })
                .connectTimeout(duration, TimeUnit.MILLISECONDS)
                .writeTimeout(duration, TimeUnit.MILLISECONDS)
                .readTimeout(duration, TimeUnit.MILLISECONDS)
                .build()

        }

        private fun getRequest(origin: Request): Request {
            return origin.newBuilder()
                .build()
        }

    }

}