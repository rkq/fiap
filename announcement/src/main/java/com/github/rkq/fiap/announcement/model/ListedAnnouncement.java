package com.github.rkq.fiap.announcement.model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Created by rick on 9/4/15.
 */
public class ListedAnnouncement {
    private String entityCode;
    private String contentAbstract;
    private String fulltextUrl;
    private Date publishDate;

    public ListedAnnouncement(String entityCode, String contentAbstract, String fulltextUrl, Date publishDate) {
        this.entityCode = entityCode;
        this.contentAbstract = contentAbstract;
        this.fulltextUrl = fulltextUrl;
        this.publishDate = publishDate;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getContentAbstract() {
        return contentAbstract;
    }

    public void setContentAbstract(String contentAbstract) {
        this.contentAbstract = contentAbstract;
    }

    public String getFulltextUrl() {
        return fulltextUrl;
    }

    public void setFulltextUrl(String fulltextUrl) {
        this.fulltextUrl = fulltextUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("entityCode", entityCode)
                .add("contentAbstract", contentAbstract)
                .add("fulltextUrl", fulltextUrl)
                .add("publishDate", publishDate)
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListedAnnouncement that = (ListedAnnouncement) o;

        return Objects.equal(this.entityCode, that.entityCode) &&
                Objects.equal(this.contentAbstract, that.contentAbstract) &&
                Objects.equal(this.fulltextUrl, that.fulltextUrl) &&
                Objects.equal(this.publishDate, that.publishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entityCode, contentAbstract, fulltextUrl, publishDate);
    }
}
