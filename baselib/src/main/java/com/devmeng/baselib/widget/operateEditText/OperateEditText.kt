package com.devmeng.baselib.widget.operateEditText

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.devmeng.baselib.R
import com.devmeng.baselib.utils.EMPTY
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.widget.ToastView

/**
 * Created by Richard -> MHS
 * Date : 2022/6/6  16:58
 * Version : 1
 * Description:携带控制器的输入框，可供 clear:全部删除 psw:密码的隐藏与显示
 */
class OperateEditText : AppCompatEditText, TextWatcher {

    companion object {
        const val OPERATOR_NONE = 0
        const val OPERATOR_CLEAR = 1
        const val OPERATOR_PSW = 2
        const val OPERATOR_CORRECT_PSW = 3
        const val OPERATOR_VISIBLE_PSW = 0x210
        const val OPERATOR_INVISIBLE_PSW = 0x220
    }

    private var operatorType = 0
    private var isPswVisible = false
    private var operatorRes = 0
    private var mWidth = 0
    private var mHeight = 0
    private val toast = ToastView(context)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeAttrs =
            context.obtainStyledAttributes(attrs, R.styleable.OperateEditText)
        with(typeAttrs) {
            //控制器类型
            operatorType = getInt(R.styleable.OperateEditText_operatorType, operatorType)
            //控制器资源
            operatorRes = getResourceId(R.styleable.OperateEditText_operatorRes, operatorRes)
            recycle()
        }
        init()
    }

    private fun init() {
        isFocusable = true
        isFocusedByDefault = true
        gravity = Gravity.CENTER_VERTICAL
        isFocusableInTouchMode = true
        var hintVar: String = context.getString(R.string.string_pls_input_account)
        if (operatorType == OPERATOR_PSW || operatorType == OPERATOR_CORRECT_PSW) {
            displayPswState()
            hintVar = context.getString(R.string.string_pls_input_psw)
            if (operatorType == OPERATOR_CORRECT_PSW) {
                hintVar = context.getString(R.string.string_pls_correct_psw)
            }
        } else if (text!!.isNotEmpty()) {
            setOperator(operatorType)
        }
        if (operatorType == OPERATOR_NONE) {
            hintVar = EMPTY
        }
        maxLines = 1
        isSingleLine = true
        compoundDrawablePadding = setDimensDp(10)
        addTextChangedListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setTextCursorDrawable(R.drawable.shape_operate_edit_text_indicator)
        }
        hint = hintVar
        tag = operatorType
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
            mHeight = if (heightMode == MeasureSpec.EXACTLY) {
                heightSize
            } else {
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60F,
                    resources.displayMetrics
                ).toInt()
            }
        } else {
            mWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200F,
                resources.displayMetrics
            ).toInt()
            mHeight = if (heightMode == MeasureSpec.EXACTLY) {
                heightSize
            } else {
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60F,
                    resources.displayMetrics
                ).toInt()
            }
        }
        setMeasuredDimension(mWidth, mHeight)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun afterTextChanged(s: Editable?) {
        val input = s.toString()
        with(input) {
            if (operatorType == OPERATOR_CLEAR) {
                when (length) {
                    0 -> {
                        setOperator(OPERATOR_NONE)
                    }
                    else -> {
                        setOperator(OPERATOR_CLEAR)
                    }
                }
            }
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if ((operatorType == OPERATOR_PSW || operatorType == OPERATOR_CORRECT_PSW) && isPswVisible) {
            transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun setOperator(type: Int) {
        operatorRes = 0
        operatorRes = when (type) {
            OPERATOR_CLEAR -> {
                R.drawable.ic_clear_30dp
            }
            OPERATOR_VISIBLE_PSW -> {
                R.drawable.ic_round_psw_visibility_30dp
            }
            OPERATOR_INVISIBLE_PSW,
            OPERATOR_CORRECT_PSW,
            OPERATOR_PSW -> {
                R.drawable.ic_round_psw_invisibility_30dp
            }
            else -> {
                operatorRes
            }
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, operatorRes, 0)
//        requestLayout()
    }

    private var downX = 0
    private var downY = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (operatorType != OPERATOR_NONE) {
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x.toInt()
                    downY = event.y.toInt()
                    Logger.d("downX -> $downX")
                }
                MotionEvent.ACTION_UP -> {
                    Logger.d("upX -> $downX")
                    return if ((downX >= mWidth - compoundPaddingEnd) and (downX < mWidth)) {
                        Logger.d("operatorType -> $operatorType")
                        when (operatorType) {
                            OPERATOR_CLEAR -> {
                                text!!.clear()
                            }
                            else -> {
                                displayPswState()
                            }
                        }
                        true
                    } else {
                        super.onTouchEvent(event)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun displayPswState() {
        when (isPswVisible) {
            true -> {
                //显示密码
                transformationMethod = HideReturnsTransformationMethod.getInstance()
                setOperator(OPERATOR_VISIBLE_PSW)
            }
            else -> {
                //隐藏密码
                transformationMethod = PasswordTransformationMethod.getInstance()
                setOperator(OPERATOR_INVISIBLE_PSW)
            }
        }
        isPswVisible = !isPswVisible
    }

    fun judgeAndToast(content: String = EMPTY): Boolean {
        when (tag) {
            OPERATOR_PSW -> {
                if (text.isNullOrEmpty()) {
                    toast.showText(context.getString(R.string.string_pls_input_psw))
                    return false
                }
                return true
            }
            OPERATOR_CORRECT_PSW -> {
                if (text.isNullOrEmpty()) {
                    toast.showText(context.getString(R.string.string_pls_correct_psw))
                    return false
                }
                if (content == text.toString()) {
                    return true
                }
                toast.showText(context.getString(R.string.string_pls_judge_not_equals))
                return false
            }
            else -> {
                if (text.isNullOrEmpty()) {
                    toast.showText(context.getString(R.string.string_pls_input_account))
                    return false
                }
                return true
            }
        }
    }

    private fun setDimensDp(size: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            size.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

}