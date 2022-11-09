package com.devmeng.baselib.entities;


import java.util.List;

/**
 * Created by Richard -> MHS
 * Date : 2022/5/21  15:30
 * Version : 1
 */
public class HomePageArticleBean {

    public int curPage;
    public List<ArticleBean> datas;
    public int offset;
    public boolean over;
    public int pageCount;
    public int size;
    public int total;

}
