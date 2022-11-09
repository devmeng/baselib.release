package com.devmeng.baselib.net

import com.devmeng.baselib.base.BaseView
import com.devmeng.baselib.entities.UserBean

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
interface Contact {

    interface SignInDataView : BaseView {

        fun onSignInCallBack(userBean: UserBean?, msg: String?)

    }

}