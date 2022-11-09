package com.devmeng.baselib.ui.sign

import android.view.View
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.base.bind.BaseBindActivity
import com.devmeng.baselib.databinding.ActivityRegisterBinding

class RegisterActivity : BaseBindActivity<BasePresenter>() {

    private var viewBinding: ActivityRegisterBinding? = null

    override fun createPresenter(): BasePresenter {
        return BasePresenter(this)
    }

    override fun initContentViewBinding(): View {
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun setConfigure() {
        with(viewBinding!!) {
            imgRegisterClose.setOnClickListener(this@RegisterActivity)
            csRegister.setOnClickListener(this@RegisterActivity)
            tvGoSignIn.setOnClickListener(this@RegisterActivity)
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        with(viewBinding!!) {
            when (v) {
                tvGoSignIn,
                imgRegisterClose -> {
                    finish()
                }
                csRegister -> {
                    if (oetAccount.judgeAndToast()) {
                        if (oetPsw.judgeAndToast(oetCorrectPsw.text.toString())) {
                            if (oetCorrectPsw.judgeAndToast(oetPsw.text.toString())) {
                                toast.showText("注册")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
    }

    override fun release() {
    }
}