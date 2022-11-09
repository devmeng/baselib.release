# 项目功能

[![](https://jitpack.io/v/devmeng/baselib.svg)](https://jitpack.io/#devmeng/baselib)

### 开发前的必需配置

1. #### **将 baselib 中的 config.gradle 文件导入到工程目录下**

2. #### **在工程目录下的 build.gradle 中应用 config.gradle配置文件，如下 apply from: 'config.gradle'**

------

### ViewBinding 

```groovy
android{
    ...
    buildFeatures {
        viewBinding = true
    }
}
```

在 baselib 中包含对使用 ViewBinding 的基准类，可使用 BaseBindActivity 等基准类实现业务逻辑

#### BaseBindActivity 基准类 Activity

```kotlin
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
        setConfigure()
        initData()
    }
        
    protected fun addFragment(
        manager: FragmentManager,
        cls: Class<out BaseBindFragment<*>>,
        container: Int,
        args: Bundle?
    ) {
    	...
    }
        
    override fun showLoading() {
    }

    override fun hideLoading() {
    }
    
    @Network(NetType.AUTO)
    fun network(netType: NetType) {
        ...
    }
    
    override fun onClick(v:View?) {
        //做统一操作
    }
    
    override fun onDestroy() {
        super.onDestroy()
        release()
        presenter!!.detachView()
    }

    override fun getViewContext(): Context {
        return WeakReference<AppCompatActivity>(this).get()!!
    }
}
```

##### BaseBindActivity 具体实现

```kotlin
class AActivity: BaseBindActivity<BasePresenter>{
    private var aBinding: ActivityABinding? = null
    override fun initContentViewBinding(): View {
        aBinding = ActivityABinding.inflate(layoutInflater)
        return mainBinding!!.root
    }

    override fun createPresenter(): BasePresenter {
        return BasePresenter(this)
    }
    
    override fun initData() {
    }

    override fun setConfigure() {
        //添加页面 Fragment
        addFragment(
            ...
        )
        with(aBinding!!) {
            imgTest.setOnClickListener(this@AActivity)
        }
    }
    
    override fun onClick(v: View?) {
        super.onClick(v)
        with(aBinding!!) {
            when(v){
                imgTest -> {
                    ...
                }
            }
        }
    }
    
    override fun release() {
        mainBinding = null
    }
}
```

------

#### BaseBindFragment 基准类 Fragment

```kotlin
abstract class BaseBindFragment<P : BasePresenter> : Fragment(), BaseView {

    protected abstract fun createPresenter(): P?

    protected abstract fun initContentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View

    protected abstract fun setConfigure()

    protected abstract fun initData()

    protected abstract fun release()

    var presenter: P? = null
    private var viewBinding: View? = null

    lateinit var fragmentActivity: FragmentActivity

    lateinit var toast: ToastView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentActivity = WeakReference(requireActivity()).get()!!
        toast = ToastView(fragmentActivity)
        presenter = createPresenter()
        viewBinding = initContentViewBinding(inflater, container)

        setConfigure()
        initData()

        return viewBinding
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

    override fun onDetach() {
        super.onDetach()
        presenter!!.detachView()
        viewBinding = null
    }

    override fun getViewContext(): Context {
        return fragmentActivity
    }

}
```

##### BaseBindFragment 具体实现

```kotlin
class AFragment : BaseBindFragment<BasePresenter>() {

    private var viewBinding: FragmentABinding? = null

    override fun createPresenter(): BasePresenter? {
        return BasePresenter(this)
    }

    override fun initContentViewBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        viewBinding = FragmentABinding.inflate(inflater, container, false)
        return viewBinding!!.root
    }

    override fun setConfigure() {
        with(viewBinding!!) {
            ...
        }
    }

    override fun initData() {
    }

    override fun release() {
    }

}
```

------

#### BaseBindAdapter 列表适配器基准类

```kotlin
abstract class BaseBindAdapter<T : Any> :
    RecyclerView.Adapter<BaseBindViewHolder>() {

    abstract fun bind(holder: BaseBindViewHolder, itemData: T, position: Int)
    abstract fun getItemViewBindingRoot(parent: ViewGroup): View

    private var mList: MutableList<T>? = arrayListOf()

    var onItemClickListener: BaseBindViewHolder.OnItemClickListener<T>? = null
    var onItemViewClickListener: BaseBindViewHolder.OnItemViewClickListener<T>? = null
    var onItemLongClickListener: BaseBindViewHolder.OnItemLongClickListener<T>? = null
    var onItemViewLongClickListener:
            BaseBindViewHolder.OnItemViewLongClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindViewHolder {
        return BaseBindViewHolder(getItemViewBindingRoot(parent))
    }

    override fun onBindViewHolder(holder: BaseBindViewHolder, position: Int) {
        val itemData = mList!![position]
        setOnItemClickListener(holder, itemData, position)
        bind(holder, itemData, position)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    /**
     * 刷新适配器，更新列表数据
     * @param list 列表数据集合
     */
    fun refreshAdapter(list: MutableList<T>) {
        refreshAdapter(list, true, list.size)
    }

    /**
     * 刷新适配器，更新列表数据
     * @param list 列表数据集合
     * @param first 是否为第一次加载
     * @param pageSize 单页数据量
     */
    fun refreshAdapter(list: MutableList<T>, first: Boolean, pageSize: Int) {
        with(mList!!) {
            when {
                //第一次添加
                first -> {
                    clear()
                    addAll(list)
                    notifyDataSetChanged()
                }
                else -> {
                    val preSize = size
                    addAll(list)
                    notifyItemRangeInserted(preSize, pageSize)
                }
            }
        }
    }

    /**
     * 倒置列表数据
     * @param list 需要倒置的数据集合
     */
    fun reverseData(list: MutableList<T>) {
        list.reverse()
        with(mList!!) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    /**
     * 列表添加子条目
     * @param itemData 实体类数据
     * @param isReverse 是否倒置
     */
    fun addItem(itemData: T, isReverse: Boolean) {
        with(mList!!) {
            when (isReverse) {
                true -> {
                    add(0, itemData)
                    notifyItemRangeInserted(0, 1)
                }
                false -> {
                    val preSize = size
                    add(itemData)
                    notifyItemRangeInserted(preSize, 1)
                }
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 移除子条目
     * @param position 需移除子条目的下标
     */
    fun removeItem(position: Int) {
        mList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    /**
     * 清理适配器，即清除列表子条目
     */
    fun clearAdapter() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    /**
     * 设置子条目点击事件
     * @param holder
     * @param itemData 当前点击的数据实体类
     * @param position 当前点击子条目下标
     */
    fun setOnItemClickListener(holder: BaseBindViewHolder, itemData: T, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(itemData, position)
        }
    }

    /**
     * 设置子条目长按事件
     * @param holder
     * @param itemData 当前长按的数据实体类
     * @param position 当前长按子条目下标
     */
    fun setOnItemLongClickListener(holder: BaseBindViewHolder, itemData: T, position: Int) {
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onLongClick(itemData, position)
            true
        }
    }

    /**
     * 设置子条目子控件的点击事件
     * @param view 点击的控件
     * @param itemData 当前点击的数据实体类
     * @param position 当前点击子条目下标
     */
    fun setOnItemViewClickListener(view: View, itemData: T, position: Int) {
        view.setOnClickListener {
            onItemViewClickListener?.onViewClick(view, itemData, position)
        }
    }

    /**
     * 设置子条目子控件的长按事件
     * @param view 长按的控件
     * @param itemData 当前长按的数据实体类
     * @param position 当前长按子条目下标
     */
    fun setOnItemViewLongClickListener(view: View, itemData: T, position: Int) {
        view.setOnLongClickListener {
            onItemViewLongClickListener?.onViewLongClick(view, itemData, position)
            true
        }
    }

}
```

##### BaseBindAdapter 具体实现

```kotlin
class RvAdapter(var context: Context, var type: Int) :
    BaseBindAdapter<TestEntity>() {
    private lateinit var viewBinding: ItemChatListFriendsBinding

    override fun getItemViewBindingRoot(parent: ViewGroup): View {
        viewBinding =
            ItemChatListFriendsBinding.inflate(LayoutInflater.from(context), parent, false)
        return viewBinding.root
    }

    override fun bind(holder: BaseBindViewHolder, itemData: TestEntity, position: Int) {
        with(viewBinding) {
            tvTest.text = itemData.testText.toString()
            ...
        }
    }

}
```

------

### 动态换肤功能

初始化动态换肤

```kotlin
class Application: Application{
    
    override fun onCreate(){
        //初始化换肤管理器，已默认在 baselib 的 BaseApplication 中初始化
        SkinManager.init(this)
    }
}
```

根据需求，在 SkinAttribute.attributeList 中增加换肤属性

```kotlin
class SkinAttribute{
    val attributeList = mutableListOf<String>(
        "background"
        ...
    )
}
```

需要创建制作皮肤包的项目，用于制作皮肤包 apk （建议将皮肤包上传至后台服务器）

在制作皮肤包的过程中注意对应需要换肤的控件属性，其资源文件名称需保持一致

在换肤业务逻辑中使用

```java
//切换皮肤
SkinManager.instance.loadSkin(skinPath)
//还原皮肤
SkinManager.instance.loadSkin()
```

------

### 全量更新/热更新功能

需要将 **baselib** 根目录下的 **tinker-support.gradle** 文件配置在 **application module** 即主应用根目录下 
并在 **build.gradle** 中配置 

```kotlin
plugins {
    id 'org.jetbrains.kotlin.android'
    ...
}
apply from: 'tinker-support.gradle' 
```

包含自定义更新弹窗 UpgradeActivity 

```

```

**注:需要将公司注册的 tinkerId 配置在 company_application_id.xml 中的 tinker_app_id 下**

------

### 组件化（根据测试需要开启）

1.gradle.properties 配置对应于每个模块在模块与 library 之间转换的判断语句或 Boolean 类型的值 
例:xxxRunAlone = true (true 为独立运行即模块形式， false 为合并模块即 library 形式)

```properties
# 模块化变量
xxxRunAlone=true
```

2.在对应模块的 build.gradle 中配置转换所需的判断语句

```java
if(xxxRunAlone.toBoolean()){
    apply plugin: 'com.android.application' 
} else {
    apply plugin: 'com.android.library' 
} 
```

3.在对应模块的 build.gradle 中配置独立运行所需的清单文件及内部的个性化配置

```java
android {
    sourceSets{
        main{ 
            if(xxxRunAlone.toBoolean()){
                manifest.srcFile 'src/main/manifest/AndroidManifest.xml' 
            } else {
                manifest.srcFile 'src/main/AndroidManifest.xml' 
            } 
        }
    } 
} 
```

4.在 App 主程序的 build.gradle 中应用对应模块 例:

```java
if(!xxxRunAlone.toBoolean()){ 
    api project(path:"xxxModule")
} 
```

------

### AspectJ 面向切面编程

**注意：《暂缓嵌入 AspectJx "RuntimeException: Zip file is empty" 问题尚未解决》** 

嵌入方法如下 
1.在工程 **build.gradle** 中导入如下代码（插入 AspectJ 插件）

```groovy
//AspectJ 面向切面编程插件 
classpath 'org.aspectj:aspectjtools:1.9.1'
classpath 'org.aspectj:aspectjweaver:1.9.1'
```

2.在模组的 build.gradle 中导入依赖 

```groovy
api rootProject.ext.aspectJ["aspectjrt"]
```

**注意：需要在 config.gradle 配置完成情况下导入**

3.在 build.gradle 中 配置 AspectJ 控制器，例如：

```groovy
//在 build.gradle 中配置控制器
//控制器可配置在 App module 或 library 中

import org.aspectj.bridge.IMessage

import org.aspectj.bridge.MessageHandler

import org.aspectj.tools.ajc.Main

final def log = project.logger

final def variants
if (isApplicationModule) {
    //App module 使用该变量
    variants = project.android.applicationVariants
} else {
    //library 使用该变量
    variants = project.android.libraryVariants
}

variants.all { variant ->

    if (!variant.buildType.isDebuggable()) {

        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")

        return

    }

    JavaCompile javaCompile = variant.javaCompile

    javaCompile.doLast {

        String[] args = ["-showWeaveInfo",

                         "-1.8",

                         "-inpath", javaCompile.destinationDirectory.toString(),

                         "-aspectpath", javaCompile.classpath.asPath,

                         "-d", javaCompile.destinationDirectory.toString(),

                         "-classpath", javaCompile.classpath.asPath,

                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]

        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true)

        new Main().run(args, handler)

        for (IMessage message : handler.getMessages(null, true)) {

            switch (message.getKind()) {

                case IMessage.ABORT:

                case IMessage.ERROR:

                case IMessage.FAIL:

                    log.error message.message, message.thrown

                    break

                case IMessage.WARNING:

                    log.warn message.message, message.thrown

                    break

                case IMessage.INFO:

                    log.info message.message, message.thrown

                    break

                case IMessage.DEBUG:

                    log.debug message.message, message.thrown

                    break

            }

        }

    }

}
```

**注意：每个使用 AspectJ 的 module 都需要在 build.gradle 中配置控制器**

------

### GreenDao 数据库的配置 

1.在项目目录下的 build.gradle 中配置插件

```groovy
classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2' 
```

2.在 app module 或 library 中应用 greenDao 插件，添加依赖，并写入相关配置

```groovy
plugins {
    ...
}
apply plugin: 'org.greenrobot.greendao' 
...
greendao { 
    //设置DaoMaster、DaoSession、Dao 一般包名+文件夹名 
    daoPackage 'com.xxx.xxx.db' 
    //数据库版本号 
    schemaVersion 1 
    //设置DaoMaster、DaoSession、Dao目录文件生成的目录，相当于父级目录 
    targetGenDir 'src/main/java' 
}
dependencies {
    //导入依赖
    api rootProject.ext.greenDaoLibs
} 
```


