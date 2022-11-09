package com.devmeng.baselib.base.bind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devmeng.baselib.annotation.Network
import com.devmeng.baselib.base.BaseAdapter
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.base.BaseView
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.utils.NetType
import com.devmeng.baselib.utils.NetworkManager
import com.devmeng.baselib.widget.ToastView
import java.lang.ref.WeakReference

/**
 * Created by Richard -> MHS
 * Date : 2022/6/15  17:46
 * Version : 1
 * Description :
 * 使用 ViewBinding 绑定对应的布局
 * 基准类包含自定义 ToastView
 * @see ToastView 可根据需求对 ToastView 的布局样式进行修改
 *
 *
 */
abstract class BaseBindActivity<P : BasePresenter> : AppCompatActivity(), BaseView,
    View.OnClickListener {

    protected lateinit var toast: ToastView

    protected abstract fun createPresenter(): P

    protected abstract fun initContentViewBinding(): View

    protected abstract fun setConfigure()

    protected abstract fun initData()

    protected abstract fun release()

    var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentViewBinding())
        toast = ToastView(applicationContext)
        presenter = createPresenter()
//        NetworkManager.instance.setNetworkChangeObserver(this)
        NetworkManager.instance.registerNetworkListener(this@BaseBindActivity)
        setConfigure()
        initData()
    }

    fun launcher(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this@BaseBindActivity, activity)
        intent.putExtras(bundle!!)
        startActivity(intent)
    }

    fun linearRecyclerView(recyclerView: RecyclerView, adapter: BaseAdapter<*>) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(v: View?) {
        //todo 统一点击事件
    }

    protected fun addFragment(
        manager: FragmentManager,
        cls: Class<out BaseBindFragment<*>>,
        container: Int,
        args: Bundle?
    ) {
        val tag = cls.name
        var fragment = manager.findFragmentByTag(tag)
        val transaction = manager.beginTransaction()
        if (fragment == null) {
            fragment = cls.newInstance()
            if (args != null) fragment.arguments = args
            transaction.add(container, fragment, tag)
            //todo 添加动画等
            val baseBindFragment = fragment as BaseBindFragment<*>
            with(baseBindFragment) {
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
        for (frags in manager.fragments) {
            if (frags != fragment) {
                transaction.hide(frags)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        release()
        presenter!!.detachView()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    @Network(NetType.AUTO)
    fun network(netType: NetType) {
        when (netType) {
            NetType.WIFI -> {
                Logger.d("当前网络 -> WIFI")
                toast.showText("正在使用 wifi", Gravity.CENTER)
            }
            NetType.CELLULAR -> {
                Logger.d("当前网络 -> CELLULAR")
                toast.showText("正在使用数据流量", Gravity.CENTER)
            }
            NetType.NONE -> {
                Logger.d("无网络")
                toast.showText("当前暂无网络", Gravity.CENTER)
            }
            NetType.INVALIDATED -> {
                Logger.d("当前网络不可用")
                toast.showText("当前网络不可用", Gravity.CENTER)
            }
            else -> {
                Logger.d("其他")
            }
        }
    }

    override fun getViewContext(): Context {
        return WeakReference<AppCompatActivity>(this).get()!!
    }

}