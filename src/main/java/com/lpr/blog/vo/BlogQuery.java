package com.lpr.blog.vo;

public class BlogQuery {
    private String title;
    private Integer typeId;
    private boolean recommended;

    public BlogQuery(String title, Integer typeId, boolean recommended) {
        this.title = title;
        this.typeId = typeId;
        this.recommended = recommended;
    }

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }
}
