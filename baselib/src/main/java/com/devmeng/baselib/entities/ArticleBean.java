package com.devmeng.baselib.entities;

import java.util.List;

/**
 * Created by Richard -> MHS
 * Date : 2022/5/31  18:18
 * Version : 1
 */
public class ArticleBean {
    /*{
            "apkLink":"", "audit":1, "author":"xiaoxiaohaozai", "canEdit":false, "chapterId":
            539, "chapterName":"未分类", "collect":false, "courseId":13, "desc":"描述"
            "", "envelopePic":
            "https://www.wanandroid.com/blogimgs/0e65b122-5ffc-4aef-8fc5-fa93b69b93a2.png", "fresh":
            true, "host":"", "id":22789, "link":
            "https://www.wanandroid.com/blog/show/3371", "niceDate":"56分钟前", "niceShareDate":
            "56分钟前", "origin":"", "prefix":"", "projectLink":
            "https://github.com/xiaoxiaohaozai/my_bill", "publishTime":
            1653316882000, "realSuperChapterId":293, "selfVisible":0, "shareDate":
            1653316882000, "shareUser":"", "superChapterId":294, "superChapterName":
            "开源项目主Tab", "tags":[{
            "name":"项目", "url":"/project/list/1?cid=539"
        }],"title":"Flutter GetX 实战", "type":0, "userId":-1, "visible":1, "zan":0
        }*/

    public String apkLink;
    public int audit;
    public String author;
    public boolean canEdit;
    public int chapterId;
    public String chapterName;
    public boolean collect;
    public int courseId;
    public String desc;
    public String descMd;
    public String envelopePic;
    public boolean fresh;
    public String host;
    public int id;
    public String link;
    public String niceDate;
    public String niceShareDate;
    public String origin;
    public String prefix;
    public String projectLink;
    public long publishTime;
    public int realSuperChapterId;
    public int selfVisible;
    public long shareDate;
    public String shareUser;
    public int superChapterId;
    public String superChapterName;
    public List<TagBean> tags;
    public String title;
    public int type;
    public int userId;
    public int visible;
    public int zan;

}
