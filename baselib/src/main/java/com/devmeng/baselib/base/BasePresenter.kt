package com.devmeng.baselib.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  10:27
 * Version : 1
 */
open class BasePresenter(var baseView: BaseView?) {

    private var compositeDisposable: CompositeDisposable? = null

    protected open fun <T> addDisposable(observable: Observable<T>, observer: BaseObserver<T>?) {
        this.compositeDisposable = CompositeDisposable()
        observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)!!
    }

    open fun removeDisposable() {
        compositeDisposable?.dispose()
    }

    fun detachView() {
        baseView = null
        removeDisposable()
    }

}