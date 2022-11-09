package com.devmeng.baselib.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.base.bind.BaseBindActivity
import com.devmeng.baselib.databinding.ActivitySearchBinding
import com.devmeng.baselib.utils.EMPTY
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.widget.OperateWidget
import com.devmeng.baselib.widget.SearchOperateView

class SearchActivity : BaseBindActivity<BasePresenter>(), OperateWidget.OnWidgetOperationListener,
    SearchOperateView.OnSearchItemClickCallback {

    companion object {
        fun launcher(activity: AppCompatActivity, bundle: Bundle?) {
            val intent = Intent(activity, SearchActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }

    private var searchBinding: ActivitySearchBinding? = null
    private var lastSearch = EMPTY
    private var searchHistory = EMPTY

    override fun initContentViewBinding(): View {
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        return searchBinding!!.root
    }

    override fun createPresenter(): BasePresenter {
        return BasePresenter(this)
    }

    override fun setConfigure() {

        with(searchBinding!!) {
            operateSearchClose.onWidgetOperation = this@SearchActivity
            operateSearchGo.onWidgetOperation = this@SearchActivity
            val list = arrayListOf("哈哈哈哈", "傻缺", "我去", "嗨害嗨", "大发送到发斯蒂芬", "阿斯蒂芬", "啊啊啊啊啊")
            val li = ArrayList<String>()

            editViewSearch.imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE
            searchOperateViewHistory.onSearchItemClickCallback = this@SearchActivity
            searchOperateViewHistory.refreshLayout(list)
        }

    }

    override fun initData() {

    }

    override fun operation(operateType: Int) {
        with(searchBinding!!) {
            if (operateType == OperateWidget.WIDGET_BACK) {
                finish()
            } else if (operateType == OperateWidget.WIDGET_SEARCH) {
                Logger.d("edit_view_search.text -> ${editViewSearch.text}")

                //todo 请求网络数据成功后执行添加操作 执行搜索网络请求
                searchHistory = editViewSearch.text.toString()
                if (searchHistory.isNotEmpty() && searchHistory != lastSearch) {
                    searchOperateViewHistory.addItem(searchHistory, true)
                    lastSearch = searchHistory
                }
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                operation(OperateWidget.WIDGET_SEARCH)
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun clickCallBack(title: String, position: Int, type: Int) {
        Logger.d("点击 -> $position [$title]")
        //todo 点击搜索记录搜索 执行搜索网络请求
    }

    override fun release() {
        searchBinding = null
    }
}