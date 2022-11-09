package com.devmeng.baselib.presenters

import com.devmeng.baselib.base.BaseObserver
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.entities.UserBean
import com.devmeng.baselib.net.Contact
import com.devmeng.baselib.net.DataService
import com.devmeng.baselib.net.HttpResult

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class SignInDataPresenter(val view: Contact.SignInDataView) : BasePresenter(view) {

    fun signIn(account: String, psw: String) {

        addDisposable(
            DataService.instance.signIn(account, psw),
            object : BaseObserver<HttpResult<UserBean>>(view) {
                override fun onSuccess(result: HttpResult<UserBean>) {
                    view.onSignInCallBack(result.data, result.errorMsg)
                }

                override fun onError(code: Int, error: String?) {
                    view.onSignInCallBack(null, error)
                }
            })

    }

}