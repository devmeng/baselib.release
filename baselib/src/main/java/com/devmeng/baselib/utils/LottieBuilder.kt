package com.devmeng.baselib.utils

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.animation.Animation
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.devmeng.baselib.R
import java.io.File

/**
 * FileName: LottieBuilder
 * Author: 孟海粟
 * Date: 2021/6/19 16:02
 * Description: Lottie 动画构造器
 * 我想起来就更新系列
 */
object LottieBuilder {
    @SuppressLint("StaticFieldLeak")
    private var sBuilder: Builder? = null
    fun builder(): Builder? {
        if (sBuilder == null) {
            sBuilder = Builder()
        }
        return sBuilder
    }

    class Builder {
        private val mImageAssets: String? = null
        private var mAssetsFolderName: String? = null
        private var mContext: Context? = null
        private var mLottie: LottieAnimationView? = null
        private val mAnimationFromUrl: String? = null

        /**
         * 必要方法 在一开始执行
         *
         * @param context
         * @return
         */
        fun with(context: Context?): Builder {
            mContext = context
            return this
        }

        /**
         * 设置 lottie 下的动画资源根目录名称
         * 建议存放 assets/lottie/动画目录
         *
         * @param folder 目录名
         * @return [Builder]
         */
        fun folderName(folder: String?): Builder {
            mAssetsFolderName = folder
            return this
        }

        /**
         * 从动画地址中截取动画所在目录的名称
         *
         * @param url 动画地址
         * @return [Builder]
         */
        fun urlParseFolderName(url: String?): Builder {
            val split: Array<String> = TextUtils.split(url, File.separator)
            val fileName = split[split.size - 1]
            mAssetsFolderName = fileName.substring(0, fileName.indexOf(DOT))
            Logger.e("fileName => $fileName")
            return this
        }

        /**
         * 从动画地址中截取动画所在目录的名称
         *
         * @param url 动画地址
         * @return [Builder]
         */
        fun urlParseFolderName(url: String?, secondFolderName: String): Builder {
            val split: Array<String> = TextUtils.split(url, File.separator)
            val fileName = split[split.size - 1]
            mAssetsFolderName = secondFolderName + File.separator + fileName.substring(
                0,
                fileName.indexOf(DOT)
            )
            Logger.e("fileName => $fileName")
            return this
        }

        /**
         * 设置图像资源
         *
         *
         * 图像存储位置  lottie/动画目录/images 必须放在 这个目录下
         * {如果 json 动画需要设置图像资源，则必须先执行 [Builder.folderName]} 方法
         * 需要使用上下文对象[Builder.with]
         *
         * @return [Builder]
         */
        fun imageAssetsFolder(): Builder {
            throwable()
            val imageAssets = mContext!!.getString(
                R.string.format_lottie_name_images_assets_folder,
                mAssetsFolderName
            )
            mLottie!!.imageAssetsFolder = imageAssets
            return this
        }

        /**
         * 设置 json 动画资源
         *
         *
         * {如果 json 资源中有引用其他图像资源，则需要先 [Builder.imageAssetsFolder] 方法
         * 需要使用上下文对象[Builder.with]
         *
         * @return [Builder]
         */
        fun jsonAssetsName(): Builder {
            val assetsAnim =
                mContext!!.getString(R.string.format_lottie_name_anim_json, mAssetsFolderName)
            mLottie!!.setAnimation(assetsAnim)
            return this
        }

        /**
         * 设置媒体资源
         *
         * @param rawRes raw 资源 需将资源放置在 res/raw 目录下
         * @return [Builder]
         */
        fun resAssetsName(@RawRes rawRes: Int): Builder {
            mLottie!!.setAnimation(rawRes)
            return this
        }

        /**
         * 设置指定动画
         *
         * @param animation 动画
         * @return [Builder]
         */
        fun animation(animation: Animation?): Builder {
            mLottie!!.animation = animation
            return this
        }

        /**
         * 设置图片资源文件
         *
         * @param drawRes 资源 id
         * @return [Builder]
         */
        fun imageResource(@DrawableRes drawRes: Int): Builder {
            mLottie!!.setImageResource(drawRes)
            return this
        }

        /**
         * 设置图片资源文件
         *
         * @param drawRes 资源 id
         * @return [Builder]
         */
        fun imageTint(color: Int): Builder {
            mLottie!!.imageTintList = ColorStateList.valueOf(color)
            return this
        }

        /**
         * 设置 drawable 图片资源
         *
         * @param drawable drawable资源
         * @return [Builder]
         */
        fun drawable(drawable: Drawable?): Builder {
            mLottie!!.setImageDrawable(drawable)
            return this
        }

        /**
         * 设置重复播放次数
         *
         * @param count {@param count int 类型数值}
         * [该参数为无限制重复][com.airbnb.lottie.LottieDrawable.INFINITE]
         * @return [Builder]
         */
        fun repeatCount(count: Int): Builder {
            mLottie!!.repeatCount = count
            return this
        }

        /**
         * 配置 LottieAnimationView
         *
         * @param lottie LottieAnimationView
         * @return [Builder]
         */
        fun lottie(lottie: LottieAnimationView?): Builder {
            mLottie = lottie
            //            if (mImageAssets != null && !mImageAssets.isEmpty()) {
//                mLottie.setImageAssetsFolder(mImageAssets);
//            }
//            if (mDrawRes == 0) {
//                if (!mAnimAssets.isEmpty()) {
//                }
//                if (mRawRes != 0) {
//                    mLottie.setAnimation(mRawRes);
//                }
//                if (mAnimation != null) {
//                    mLottie.setAnimation(mAnimation);
//                }
//                return this;
//            }
//            mLottie.setImageResource(mDrawRes);
            return this
        }

        /**
         * 设置动画监听器
         * [Builder.lottie]
         * 必须在设置 LottieAnimationView 对象之后才可以使用该方法
         *
         * @return [Builder]
         */
        fun listener(animatorListener: Animator.AnimatorListener?): Builder {
            mLottie!!.addAnimatorListener(animatorListener)
            return this
        }

        /**
         * 执行动画
         * [Builder.lottie]
         * 必须在设置 LottieAnimationView 对象之后才可以使用该方法
         */
        fun play() {
            if (mLottie == null) {
                throw NullPointerException("请调用 lottie(LottieAnimationView) 以配置您的 LottieAnimationView")
            }
            mLottie!!.playAnimation()
        }

        /**
         * 清除原有动画
         * [Builder.lottie]
         * 必须在设置 LottieAnimationView 对象之后才可以使用该方法
         *
         * @return [Builder]
         */
        fun clear(): Builder {
            mLottie!!.clearAnimation()
            return this
        }

        private fun throwable() {
            if (mLottie == null) {
                throw NullPointerException("请调用 lottie(LottieAnimationView) 以配置您的 LottieAnimationView")
            } else if (mAssetsFolderName == null || mAssetsFolderName!!.isEmpty()) {
                val throwable = Throwable()
                throwable.stackTrace = arrayOf(
                    StackTraceElement(
                        "com.hssenglish.baselibrary.utils.LottieBuilder",
                        "imageAssetsFolder",
                        "LottieBuilder",
                        throwable.stackTrace[0].lineNumber
                    )
                )
                throw IllegalStateException(
                    "请先调用 folderName(String) 方法设置图像资源的上一级目录，且该方法需要在 imageAssetsFolder() 及 jsonAssetsName(String) 方法之前调用!",
                    throwable
                )
            }
        }
    }
}