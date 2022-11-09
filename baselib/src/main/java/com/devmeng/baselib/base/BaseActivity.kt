package com.devmeng.baselib.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Richard -> MHS
 * Date : 2022/5/26  16:44
 * Version : 1
 */
abstract class BaseActivity<P : BasePresenter> : AppCompatActivity(), BaseView,
    View.OnClickListener {

    protected abstract fun createPresenter(): P

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun setConfigure()

    /**
     * 释放资源
     */
    protected abstract fun release()

    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        presenter = createPresenter()
        initView()
        setConfigure()
    }

    fun launcher(activity: AppCompatActivity, bundle: Bundle) {
        val intent = Intent(this@BaseActivity, activity.javaClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        //TODO 自定义的统一操作
    }

    protected fun addFragment(
        manager: FragmentManager,
        container: Int,
        aClass: Class<out BaseFragment<*>>,
        args: Bundle?
    ) {
        val transaction = manager.beginTransaction()
        var fragment: Fragment? = manager.findFragmentByTag(aClass.name)
        if (fragment == null) {
            fragment = aClass.newInstance()
            if (args != null) {
                fragment.arguments = args
            }
            val baseFragment = fragment as BaseFragment<*>
            with(baseFragment) {
                if (isNeed2Add2BackStack()) {
                    transaction.addToBackStack(tag)
                }
                if (isNeedAddAnimation()) {
                    transaction.setCustomAnimations(
                        fragmentEnterAnim(),
                        fragmentExitAnim(),
                        fragmentPopEnterAnim(),
                        fragmentPopExitAnim()
                    )
                }
            }
            transaction.add(container, fragment, aClass.name)
        } else {
            if (fragment.isHidden) {
                hideOtherFragment(manager, transaction, fragment)
                transaction.show(fragment)
            }
        }
        transaction.commit()
    }

    private fun hideOtherFragment(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        fragment: Fragment
    ) {
        for (frag in manager.fragments) {
            if (fragment != frag) {
                transaction.hide(frag)
            }
        }
    }

    fun <V : View> getView(@IdRes id: Int): V {
        return findViewById(id)
    }

    /**
     * 开启缓存线程池
     *
     * @param runnable [BaseRunnable]
     * @return [ExecutorService]
     * [返回值 用于在页面销毁时 回收线程池][ExecutorService.shutdown]
     */
    open fun newCacheThreadPool(runnable: BaseRunnable?): ExecutorService? {
        val threadPool = Executors.newCachedThreadPool()
        threadPool.submit(runnable)
        threadPool.shutdown()
        return threadPool
    }

    /**
     * 开启可调度线程池
     *
     * @param corePoolSize 核心线程数
     * @param runnable     [BaseRunnable]
     * @return [ExecutorService]
     * [返回值 用于在页面销毁时 回收线程池][ExecutorService.shutdown]
     */
    open fun newScheduledThreadPool(corePoolSize: Int, runnable: BaseRunnable?): ExecutorService? {
        val threadPool: ExecutorService = Executors.newScheduledThreadPool(corePoolSize)
        threadPool.submit(runnable)
        threadPool.shutdown()
        return threadPool
    }

    /**
     * 开启固定大小的线程池
     *
     * @param nThreads 线程数量
     * @param runnable [BaseRunnable]
     * @return [ExecutorService]
     * [返回值 用于在页面销毁时 回收线程池][ExecutorService.shutdown]
     */
    open fun newFixedThreadPool(nThreads: Int, runnable: BaseRunnable?): ExecutorService? {
        val threadPool = Executors.newFixedThreadPool(nThreads)
        threadPool.submit(runnable)
        threadPool.shutdown()
        return threadPool
    }

    /**
     * 开启单线程线程池
     *
     * @param runnable [BaseRunnable]
     * @return [ExecutorService]
     * [返回值 用于在页面销毁时 回收线程池][ExecutorService.shutdown]
     */
    open fun newSingleThreadExecutor(runnable: BaseRunnable?): ExecutorService? {
        val threadPool = Executors.newSingleThreadExecutor()
        threadPool.submit(runnable)
        threadPool.shutdown()
        return threadPool
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
        presenter!!.detachView()

    }

}