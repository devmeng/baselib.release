package com.devmeng.baselib.skin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import com.devmeng.baselib.utils.EMPTY
import com.devmeng.baselib.utils.Logger

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class SkinResources private constructor() {

    lateinit var context: Context
    private var skinResources: Resources? = null
    private var skinPkgName: String = EMPTY
    private var isDefaultSkin = true

    companion object {

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinResources()
        }

        fun init(context: Context): SkinResources {
            instance.context = context
            return instance
        }

    }

    fun getDimension(dimens: Int): Float {
        val skinRes = getIdentifier(dimens)
        if (isDefaultSkin.or(skinRes == 0)) {
            return context.resources.getDimension(dimens)
        }
        return skinResources!!.getDimension(skinRes)
    }

    fun getColor(color: Int): Int {
        val skinRes = getIdentifier(color)
        if (isDefaultSkin.or(skinRes == 0)) {
            return context.getColor(color)
        }
        return skinResources!!.getColor(skinRes, null)
    }

    fun getColorId(color: Int): Int {
        val skinRes = getIdentifier(color)
        if (isDefaultSkin.or(skinRes == 0)) {
            return color
        }
        return skinRes
    }

    fun getColorStateList(color: Int): ColorStateList? {
        val skinRes = getIdentifier(color)
        if (isDefaultSkin.or(skinRes == 0)) {
            return context.getColorStateList(color)
        }
        return skinResources?.getColorStateList(skinRes, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(resId: Int): Drawable? {
        val skinRes = getIdentifier(resId)
        if (isDefaultSkin.or(skinRes == 0)) {
            return context.getDrawable(resId)
        }
        return skinResources!!.getDrawable(skinRes, null)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableId(resId: Int): Int {
        val skinRes = getIdentifier(resId)
        if (isDefaultSkin.or(skinRes == 0)) {
            return resId
        }
        return skinRes
    }

    fun getBackground(resId: Int): Any? {
        val resTypeName = context.resources.getResourceTypeName(resId)

        return if ("color" == resTypeName) {
            getColor(resId)
        } else {
            getDrawable(resId)
        }
    }

    fun getBoolean(resId: Int): Boolean {
        val skinRes = getIdentifier(resId)
        if (isDefaultSkin.or(skinRes == 0)) {
            return context.resources.getBoolean(resId)
        }
        return skinResources!!.getBoolean(skinRes)
    }

    fun getTypeface(typefaceId: Int): Typeface {
        val typefacePath = getSkinString(typefaceId)
        if (typefacePath.isEmpty()) {
            return Typeface.DEFAULT
        }
        try {
            if (isDefaultSkin) {
                //如果是默认皮肤，且字体路径不为空时创建字体
                // （即：app -> value.xml / value-night.xml 中 skin_ttf_* 的参数不为空）
                return Typeface.createFromAsset(context.resources.assets, typefacePath)
            }
            return Typeface.createFromAsset(skinResources?.assets, typefacePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Typeface.DEFAULT
    }

    private fun getSkinString(skinTypeFaceId: Int): String {
        if (skinTypeFaceId == 0) {
            throw Resources.NotFoundException("请在 theme.xml 中配置 skinTypeface 属性")
        }
        if (isDefaultSkin) {
            return context.resources.getString(skinTypeFaceId)
        }
        val resId = getIdentifier(skinTypeFaceId)
        if (resId == 0) {
            return context.resources.getString(skinTypeFaceId)
        }
        return skinResources!!.getString(resId)
    }

    /**
     * 应用皮肤
     * @param resources 客制化皮肤 Resources
     * @param pkgName 皮肤包所在的 package
     * @see com.devmeng.baselib.skin.SkinManager.loadSkin
     */
    fun applySkin(resources: Resources, pkgName: String) {
        skinResources = resources
        skinPkgName = pkgName
        isDefaultSkin = pkgName.isEmpty()
    }

    /**
     * 获取标识符
     * @param resId 皮肤包中皮肤属性对应的资源 id 例: @drawable/icon 对应的 id: Int
     */
    fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }
        //例如: @drawable/icon -> drawable
//        val resName = context.resources.getResourceName(resId)
        val typeName = context.resources.getResourceTypeName(resId)
        //例如: @drawable/icon -> icon
        val entryName = context.resources.getResourceEntryName(resId)
//        Logger.d("resName -> $resName")
        Logger.d("typeName -> $typeName")
        Logger.d("entryName -> $entryName")
//        Logger.d("skinPkgName -> $skinPkgName")
        val skinIdentifier = skinResources?.getIdentifier(entryName, typeName, skinPkgName)
        Logger.d("skinIdentifier -> $skinIdentifier")
        return skinIdentifier!!
    }

    fun reset() {
        skinResources = null
        skinPkgName = EMPTY
        isDefaultSkin = true
    }

}