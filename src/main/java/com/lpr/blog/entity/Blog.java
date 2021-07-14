package com.lpr.blog.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity //说明是实体类，生成数据库表
@Table(name = "t_blog")
public class Blog {
    private String title;
    @Lob
    @Basic(fetch = FetchType.LAZY) //懒加载
    private String content;
    @Id //主键
    @GeneratedValue
    private Integer id;
    private int views;
    private String flag;
    private boolean shared;
    private boolean allowAppreciate;
    private boolean allowComment;
    private boolean published;
    private boolean recommended;

    @Transient
    private String tagIds;

    //必须加注解才能在数据库生成时间戳
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne //一对多中多的一端, 多端负责维护关系
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    //级联关系，新增blog时如果有新tag，就级联新增
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    public Blog() {
    }

    public void initTagIds(){
        StringBuffer buffer = new StringBuffer();
        int i;
        for(i = 0; i < tags.size()-1; i++){
            buffer.append(tags.get(i).getId()).append(",");
        }
        buffer.append(tags.get(i).getId());
        tagIds = buffer.toString();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", views=" + views +
                ", flag='" + flag + '\'' +
                ", allowShare=" + shared +
                ", allowAppreciate=" + allowAppreciate +
                ", allowComment=" + allowComment +
                ", published=" + published +
                ", getRecommended=" + recommended +
                ", publishDate=" + publishDate +
                ", updateDate=" + updateDate +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean getShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isAllowAppreciate() {
        return allowAppreciate;
    }

    public void setAllowAppreciate(boolean allowAppreciate) {
        this.allowAppreciate = allowAppreciate;
    }

    public boolean isAllowComment() {
        return allowComment;
    }

    public void setAllowComment(boolean allowComment) {
        this.allowComment = allowComment;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }
}
