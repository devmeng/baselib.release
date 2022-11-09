package com.devmeng.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

/**
 * Created by Richard -> MHS
 * Date : 2022/5/26  17:23
 * Version : 1
 */
abstract class BaseFragment<P : BasePresenter> : Fragment(), BaseView, View.OnClickListener {

    protected abstract fun createPresenter(): P

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(inflate: View)

    protected abstract fun setConfigure()

    lateinit var fragmentActivity: FragmentActivity
    lateinit var presenter: P
    private lateinit var inflate: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentActivity = WeakReference(requireActivity()).get()!!
        presenter = createPresenter()
        inflate = inflater.inflate(getLayoutId(), container, false)
        initView(inflate)
        setConfigure()
        return inflate
    }

    override fun onClick(v: View?) {
        //TODO 处理自定义统一操作
    }

    fun <V : View> getView(@IdRes id: Int): V {
        return inflate.findViewById(id)
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

    override fun getViewContext(): Context {
        return fragmentActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter!!.detachView()
    }

}