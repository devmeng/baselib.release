package com.devmeng.baselib.skin

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import com.devmeng.baselib.skin.utils.SkinPreference
import com.devmeng.baselib.skin.utils.SkinResources
import com.devmeng.baselib.utils.EMPTY
import com.devmeng.baselib.utils.Logger
import java.util.*

/**
 * Created by Richard
 * Version : 1
 * Description :
 * 皮肤管理类:
 * 1.通过反射 AssetManager 将皮肤的 asset 资源文件路径添加到 mApkAssets[] 该集合中
 * 并与新建的 Resources 类做关联（ Resources constructor 已经弃用建议后期修改为《/*注释内容*/》并处理有关 Bug）
 * @see AssetManager
 * @see Resources
 * 2.通过 PackageManager 获取皮肤包所在的 apk 的包名，以获取皮肤包内的资源
 * @see PackageManager
 * 3.通知观察者
 *
 */
class SkinManager private constructor() : Observable(){

    private lateinit var application: Application

    companion object {

        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinManager()
        }

        @JvmStatic
        fun init(application: Application): SkinManager {
            application.registerActivityLifecycleCallbacks(SkinActivityLifecycle())
            SkinPreference.init(application.applicationContext)
            SkinResources.init(application.applicationContext)
            Logger.d("skinResource context -> ${SkinResources.init(application.applicationContext).context}")
            instance.application = application
            return instance
        }
    }

    /**
     * 加载皮肤包
     * @param skinPath 如果皮肤包路径不为空则加载皮肤，反之还原皮肤
     * 1.使用 SkinResources 通过自定义的 Resources 和 AssetManager
     * 加载 PackageManager 获取的外部 apk 皮肤包
     * @see SkinResources.applySkin
     * 2.使用 SkinPreference 储存皮肤包路径
     * @see SkinPreference
     * 3.通知观察者
     */
    fun loadSkin(skinPath: String = EMPTY) {
        if (skinPath.isNotEmpty()) {
            try {
                Logger.d("skinPath -> $skinPath")
                val assetManager = AssetManager::class.java.newInstance()
                /*val method =
                    assetManager.javaClass.getMethod(
                        "addAssetPathInternal",
                        String::class.java,
                        Boolean::class.java,
                        Boolean::class.java
                    )*/
                val method = assetManager.javaClass.getMethod(
                    "addAssetPath",
                    String::class.java
                )
//                method.invoke(assetManager, skinPath, false, false)
                method.invoke(assetManager, skinPath)
                val resources = application.resources
                //通过反射获取 ResourceImpl 并将 mAssets 等变量赋值

                /*
                val resourcesImpl =
                    Resources::class.java.classLoader!!
                        .loadClass("android.content.res.ResourcesImpl")

                val assets = resourcesImpl.getDeclaredField("mAssets")
                val metrics = resourcesImpl.getDeclaredField("mMetrics")
                val config = resourcesImpl.getDeclaredField("mConfiguration")
                metrics.isAccessible = true
                config.isAccessible = true

                assets.set(resourcesImpl, assetManager)
                metrics.set(resourcesImpl, resources.displayMetrics)
                config.set(resourcesImpl, resources.configuration)

                val mResourcesImpl = Resources::class.java.getField("mResourcesImpl")

                mResourcesImpl.set(resources, resourcesImpl)
*/
                val skinResources =
                    Resources(assetManager, resources.displayMetrics, resources.configuration)

                //获取 skinPath 所在的 Apk 包名
                val pkgManager = application.packageManager
                val packageArchiveInfo =
                    pkgManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                if (packageArchiveInfo != null) {
                    val pkgName = packageArchiveInfo.packageName
                    //存储并应用皮肤包资源，此时还没有进行皮肤的切换
                    SkinPreference.instance.setSkin(skinPath)
                    SkinResources.instance.applySkin(skinResources, pkgName)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            //还原皮肤
            SkinPreference.instance.setSkin()
            SkinResources.instance.reset()
        }
        //通知观察者并在观察者的 update 方法中进行皮肤的应用
        setChanged()
        notifyObservers()
    }

}