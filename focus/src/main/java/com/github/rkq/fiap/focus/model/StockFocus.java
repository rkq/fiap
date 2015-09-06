package com.github.rkq.fiap.focus.model;

import com.google.common.base.Objects;

/**
 * Created by rick on 9/5/15.
 */
public class StockFocus {
    private int sort;
    private String code;
    private String name;
    private int rate;
    private long version;

    public StockFocus(int sort, String code, String name, int rate, long version) {
        this.sort = sort;
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.version = version;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockFocus that = (StockFocus) o;

        return Objects.equal(this.sort, that.sort) &&
                Objects.equal(this.code, that.code) &&
                Objects.equal(this.name, that.name) &&
                Objects.equal(this.rate, that.rate) &&
                Objects.equal(this.version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sort, code, name, rate, version);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("sort", sort)
                .add("code", code)
                .add("name", name)
                .add("rate", rate)
                .add("version", version)
                .toString();
    }
}
