package com.devmeng.baselib.skin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import com.devmeng.baselib.skin.utils.SkinPreference
import com.devmeng.baselib.skin.utils.SkinThemeUtils

/**
 * Created by Richard
 * Version : 1
 * Description :
 * 以切面编程的形式为每个 Activity 配置其相应的 LayoutFactory 从做换肤的准备
 * 并且对每一个 Factory 进行观察者注册
 *
 */
class SkinActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private val factoryMap: HashMap<Activity, SkinLayoutFactory> = hashMapOf()

    @SuppressLint("DiscouragedPrivateApi")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //防止重启状态栏还原
//        SkinThemeUtils.updateStatusBarState(activity)
        //加载皮肤包 字体
        val skinTypeface = SkinThemeUtils.getSkinTypeface(activity)
        val layoutInflater = LayoutInflater.from(activity)
        try {
            //根据源码中 setFactory2 方法需将 mFactorySet 先设置成 false，才可使用
            val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val factory = SkinLayoutFactory(activity, skinTypeface)
        layoutInflater.factory2 = factory
        //注册观察者
        SkinManager.instance.addObserver(factory)
        factoryMap[activity] = factory
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        //防止重启状态栏还原
        SkinThemeUtils.updateStatusBarState(activity)

        SkinManager.instance.loadSkin(SkinPreference.instance.getSkin())
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        //取消观察者
        val factory = factoryMap.remove(activity)
        SkinManager.instance.deleteObserver(factory)
    }
}