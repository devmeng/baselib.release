package com.devmeng.baselib.base

import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  10:51
 * Version : 1
 */
abstract class BaseObserver<T>(private var view: BaseView) : DisposableObserver<T>() {

    abstract fun onSuccess(result: T)

    abstract fun onError(code: Int, error: String?)

    override fun onStart() {
        view.showLoading()
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        val exception: Exception?

        if (e is HttpException) {
            exception = e
            val response = exception.response()!!
            val code = response.code()
            val message = response.message()
            onError(code, message)
        }
        onError(0, e.message)
    }

    override fun onComplete() {
        view.hideLoading()
        dispose()
    }

}