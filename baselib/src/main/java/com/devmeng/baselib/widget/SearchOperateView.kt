package com.devmeng.baselib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devmeng.baselib.R
import com.devmeng.baselib.base.BaseAdapter
import com.devmeng.baselib.base.BaseViewHolder
import com.devmeng.baselib.utils.EMPTY
import com.devmeng.baselib.utils.Logger
import java.lang.ref.WeakReference

/**
 * Created by Richard -> MHS
 * Date : 2022/6/10  18:19
 * Version : 1
 */
class SearchOperateView : LinearLayoutCompat,
    BaseViewHolder.OnItemClickListener<String>,
    BaseViewHolder.OnItemViewClickListener<String> {

    companion object {
        const val LINEAR_VERTICAL = 1
        const val GRID_TYPE = 2

        const val TYPE_HISTORY = 0x100
    }

    private var mWidth = 0
    private var mHeight = 0

    //自定义属性
    private var padding = 0
    private var paddingVertical = 0
    private var paddingHorizontal = 0
    private var operateTitle = EMPTY
    private var titleColor = R.color.color_black_333
    private var tvBottomClearTxtColor = R.color.color_gray_6C6C6C
    private var titleSize = 16
    private var itemTxtSize = 14
    private var spaceBetween = 10
    private var itemHeight = 40
    private var searchOperateViewType = LINEAR_VERTICAL
    private var gridColumns = 2
    private var initialDataCount = 2

    private var dataType: Int = TYPE_HISTORY

    //子控件 - 标题
    private var tvTitle: TextView? = null

    //子控件 - 列表
    private var rvOperate: RecyclerView? = null
    private var rvAdapter: SearchOperateViewAdapter? = null

    //子控件 - 底部清除控件
    private var tvBottomOperator: TextView? = null

    //数据
    private var dataList: MutableList<String>? = null

    var onSearchItemClickCallback: OnSearchItemClickCallback? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("ResourceAsColor")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.SearchOperateView)
        with(typedArray) {
            padding = getDimension(
                R.styleable.SearchOperateView_android_padding,
                padding.toFloat()
            ).toInt()
            paddingVertical = getDimension(
                R.styleable.SearchOperateView_android_paddingVertical,
                paddingVertical.toFloat()
            ).toInt()
            paddingHorizontal = getDimension(
                R.styleable.SearchOperateView_android_paddingHorizontal,
                paddingHorizontal.toFloat()
            ).toInt()

            operateTitle =
                getString(R.styleable.SearchOperateView_operateTitle).toString()

            titleColor = getColor(
                R.styleable.SearchOperateView_titleColor,
                titleColor
            )
            tvBottomClearTxtColor = getColor(
                R.styleable.SearchOperateView_tvBottomClearTxtColor,
                tvBottomClearTxtColor
            )
            titleSize =
                getDimension(
                    R.styleable.SearchOperateView_titleSize,
                    titleSize.toFloat()
                ).toInt()
            itemTxtSize =
                getDimension(
                    R.styleable.SearchOperateView_itemTxtSize,
                    itemTxtSize.toFloat()
                ).toInt()
            spaceBetween =
                getDimension(
                    R.styleable.SearchOperateView_spaceBetween,
                    spaceBetween.toFloat()
                ).toInt()
            itemHeight =
                getDimension(
                    R.styleable.SearchOperateView_itemHeight,
                    itemHeight.toFloat()
                ).toInt()
            searchOperateViewType = getInt(
                R.styleable.SearchOperateView_searchOperateViewType,
                searchOperateViewType
            )
            dataType = getInt(
                R.styleable.SearchOperateView_dataType,
                dataType
            )
            gridColumns = getInt(
                R.styleable.SearchOperateView_gridColumns,
                gridColumns
            )
            initialDataCount = getInt(
                R.styleable.SearchOperateView_initialDataCount,
                initialDataCount
            )

            recycle()
        }
//        initPlaceHolder()
        initTitle()
    }

    /**
     * 用于无搜索记录的缺省状态
     */
    private fun initPlaceHolder() {
        val imgPlaceHolder = AppCompatImageView(context)

        addView(imgPlaceHolder)
    }

    /**
     * 刷新列表数据及适配器
     * @param list 集合数据
     */
    fun refreshLayout(list: MutableList<String>) {
        list.reverse()
        with(rvOperate!!) {
            layoutManager = initRecyclerConfig()
            rvOperate!!.overScrollMode = OVER_SCROLL_NEVER

            rvAdapter = SearchOperateViewAdapter(this@SearchOperateView, searchOperateViewType)
            adapter = rvAdapter

            with(rvAdapter!!) {
                mOnItemClickListener = this@SearchOperateView
                mOnItemViewClickListener = this@SearchOperateView

                refreshAdapter(list, true, initialDataCount)
            }
            val marginLayoutCompat =
                configRvLayoutParams(
                    LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        (itemHeight * initialDataCount).toFloat(), resources.displayMetrics
                    ).toInt()
                )
            rvOperate!!.layoutParams = marginLayoutCompat

//            addItemDecoration(DividerItemDecoration(context, VERTICAL))
//            addItemDecoration(DividerItemDecoration(context, HORIZONTAL))
        }
        addView(rvOperate)
        visibility = View.VISIBLE
        if (dataType == TYPE_HISTORY && list.size > initialDataCount) {
            initClearArea()
        }
        requestLayout()
    }

    /**
     * 初始化区域标题
     */
    private fun initTitle() {
        tvTitle = TextView(context)
        rvOperate = RecyclerView(context)
        rvOperate!!.isScrollContainer = false
        orientation = VERTICAL

        with(tvTitle!!) {
            text = operateTitle
            maxLines = 1
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.MARQUEE
            setTextColor(context.getColor(titleColor))
            textSize = titleSize.toFloat()
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            addView(this)
        }

        visibility = View.GONE

    }

    /**
     * 初始化列表配置(布局管理器)
     */
    private fun initRecyclerConfig(): RecyclerView.LayoutManager = when (searchOperateViewType) {
        LINEAR_VERTICAL -> {
//            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
        else -> {
//            GridLayoutManager(context, gridColumns, RecyclerView.VERTICAL, false)
            object : GridLayoutManager(context, gridColumns, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    private fun configRvLayoutParams(width: Int, height: Int): MarginLayoutParams {
        var marginLayoutCompat =
            MarginLayoutParams(ViewGroup.LayoutParams(width, height))
        marginLayoutCompat.topMargin = spaceBetween
        return marginLayoutCompat
    }

    /**
     * 初始化底部控制功能控件
     */
    private fun initClearArea() {
        tvBottomOperator = TextView(context)
        with(tvBottomOperator!!) {
            tvBottomOperator!!.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            this.setPadding(0, 20, 0, 20)

            text = context.getString(R.string.string_all_search_history)

            setTextColor(context.getColor(tvBottomClearTxtColor))

            gravity = Gravity.CENTER

            this.setOnClickListener {
                when (text) {
                    context.getString(R.string.string_all_search_history) -> {
                        text = context.getString(R.string.string_clear_all_search_history)
                        rvOperate!!.layoutParams = LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    context.getString(R.string.string_clear_all_search_history) -> {
                        text = context.getString(R.string.string_all_search_history)
                        rvAdapter!!.clearAdapter()
                        this@SearchOperateView.visibility = GONE
                    }
                }
            }
        }

        addView(tvBottomOperator)
    }

    /**
     * 添加子条目
     * @param itemData 数据
     * @param isReverse 是否为倒序
     */
    fun addItem(itemData: String, isReverse: Boolean) {
        with(rvAdapter!!) {
            this.addItem(itemData, isReverse)
        }
        visibility = View.VISIBLE
    }

    /**
     * 子条目点击事件
     */
    override fun onItemClick(holder: BaseViewHolder, itemData: String, position: Int) {
        onSearchItemClickCallback!!.clickCallBack(itemData, position, TYPE_HISTORY)
    }

    /**
     * 子条目控件点击事件
     */
    override fun onViewClick(
        holder: BaseViewHolder,
        view: View,
        itemData: String,
        position: Int
    ) {
        rvAdapter!!.removeItem(position)
        Logger.d("移除->$position [$itemData]")
        if (rvAdapter!!.mList!!.size == 0) {
            visibility = View.GONE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val widthOffset = widthOffset()
        val heightOffset = heightOffset()

        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                mWidth = widthSize + widthOffset
                mHeight = if (heightMode == View.MeasureSpec.EXACTLY) {
                    heightSize + heightOffset
                } else {
                    calcHeight(heightOffset)
                }
            }
            MeasureSpec.AT_MOST -> {
                mWidth = widthSize + widthOffset
                mHeight = if (heightMode == MeasureSpec.EXACTLY) {
                    heightSize + heightOffset
                } else {
                    calcHeight(heightOffset)
                }
            }
            else -> {
                mWidth = wrapMeasureSpec(0, rvOperate!!.measuredWidth, 0, widthOffset)

                mHeight = if (heightMode == MeasureSpec.EXACTLY) {
                    heightSize + heightOffset
                } else {
                    calcHeight(heightOffset)
                }
            }
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    /**
     * 自适应情况下的尺寸计算方法
     */
    private fun wrapMeasureSpec(tvWH: Int, rvWH: Int, tvAllClear: Int, offset: Int) =
        tvWH + rvWH + tvAllClear + offset

    /**
     * 根据不同 View 类型计算高度
     */
    private fun calcHeight(offset: Int) =
        wrapMeasureSpec(
            tvTitle!!.measuredHeight,
            rvOperate!!.measuredHeight,
            tvBottomOperator!!.measuredHeight,
            offset
        )

    /**
     * 宽度偏移量
     */
    private fun widthOffset() = when {
        padding != 0 -> padding * 2

        paddingHorizontal != 0 -> paddingHorizontal * 2

        else -> 0

    }

    /**
     * 高度偏移量
     */
    private fun heightOffset() = when {
        padding != 0 -> padding * 2

        paddingVertical != 0 -> paddingVertical * 2

        else -> 0
    }

    internal class SearchOperateViewAdapter :
        BaseAdapter<String> {

        private var searchView: SearchOperateView? = null
        var itemLayoutRes = 0

        constructor(
            searchOperateView: SearchOperateView,
            type: Int
        ) : this(R.layout.item_search_operate_view_layout, searchOperateView, type)

        constructor(
            @LayoutRes itemLayout: Int,
            searchOperateView: SearchOperateView,
            type: Int
        ) : super(searchOperateView.context, type) {
            searchView = WeakReference(searchOperateView).get()
            itemLayoutRes = itemLayout
        }

        override fun getItemLayoutId(): Int {
            return itemLayoutRes
        }

        override fun bind(holder: BaseViewHolder, itemData: String, position: Int) {

            val itemRecordContainer = holder.getView<ConstraintLayout>(R.id.item_record_container)
            val itemTvTitle = holder.getView<TextView>(R.id.item_tv_record_title)
            val itemImgClear = holder.getView<AppCompatImageView>(R.id.item_img_record_clear)
//            itemRecordContainer.layoutParams =
//                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, searchView!!.itemHeight)
            itemRecordContainer.minHeight = searchView!!.itemHeight
            with(itemTvTitle) {
                text = itemData
                textSize = searchView!!.itemTxtSize.toFloat()
            }
            setOnItemViewClickListener(holder, itemImgClear, itemData, position)
        }

    }

    /**
     * 子条目点击的接口回调
     */
    interface OnSearchItemClickCallback {
        /**
         * 点击回调
         * @param title 标题
         * @param position 索引
         */
        fun clickCallBack(title: String, position: Int, type: Int)
    }

}