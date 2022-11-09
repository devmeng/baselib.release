package com.devmeng.baselib.ui.sign

import android.os.Bundle
import android.view.View
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.base.bind.BaseBindActivity
import com.devmeng.baselib.databinding.ActivitySignInBinding

class SignInActivity : BaseBindActivity<BasePresenter>() {

    private var viewBinding: ActivitySignInBinding? = null

    override fun createPresenter(): BasePresenter {2
        return BasePresenter(this)
    }

    override fun initContentViewBinding(): View {
        viewBinding = ActivitySignInBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun setConfigure() {
        with(viewBinding!!) {
            csSignInBtn.setOnClickListener(this@SignInActivity)
            imgSignInClose.setOnClickListener(this@SignInActivity)
            tvRegister.setOnClickListener(this@SignInActivity)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        with(viewBinding!!) {
            when (v) {
                csSignInBtn -> {
                    if (oetAccount.judgeAndToast()) {
                        if (oetPsw.judgeAndToast()) {
                            toast.showText("登录")
                        }
                    }
                }
                imgSignInClose -> {
                    finish()
                }
                tvRegister -> {
                    launcher(RegisterActivity::class.java, Bundle())
                }
            }
        }
    }

    override fun initData() {
    }

    override fun release() {
    }
}