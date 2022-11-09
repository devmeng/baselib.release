package com.devmeng.baselib.base.bind

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.base.BaseView
import com.devmeng.baselib.widget.ToastView
import java.lang.ref.WeakReference

/**
 * Created by Richard -> MHS
 * Date : 2022/6/15  18:16
 * Version : 1
 * Description :
 */
abstract class BaseBindFragment<P : BasePresenter> : Fragment(), BaseView {

    protected abstract fun createPresenter(): P?

    protected abstract fun initContentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View

    protected abstract fun setConfigure()

    protected abstract fun initData()

    protected abstract fun release()

    var presenter: P? = null
    private var viewBinding: View? = null

    lateinit var fragmentActivity: FragmentActivity

    lateinit var toast: ToastView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentActivity = WeakReference(requireActivity()).get()!!
        toast = ToastView(fragmentActivity)
        presenter = createPresenter()
        viewBinding = initContentViewBinding(inflater, container)

        setConfigure()
        initData()

        return viewBinding
    }

    /**
     * 是否加入回退栈
     * @return default false 不加入
     */
    fun isNeed2Add2BackStack(): Boolean = false

    /**
     * 是否加入动画
     * @return default false 不添加动画
     */
    fun isNeedAddAnimation(): Boolean = false

    /**
     * 进场动画
     */
    fun fragmentEnterAnim(): Int = 0

    /**
     * 出场动画
     */
    fun fragmentExitAnim(): Int = 0

    /**
     * 重生进场动画
     */
    fun fragmentPopEnterAnim(): Int = 0

    /**
     * 弹出出场动画
     */
    fun fragmentPopExitAnim(): Int = 0

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onDetach() {
        super.onDetach()
        presenter!!.detachView()
        viewBinding = null
    }

    override fun getViewContext(): Context {
        return fragmentActivity
    }

}