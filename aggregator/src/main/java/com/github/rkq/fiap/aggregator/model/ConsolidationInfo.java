package com.github.rkq.fiap.aggregator.model;

import com.google.common.base.Objects;

/**
 * Created by rick on 9/5/15.
 */
public class ConsolidationInfo {
    private String code;
    private String name;
    private int focusRate;
    private int holdingChange;

    public ConsolidationInfo(String code, String name, int focusRate, int holdingChange) {
        this.code = code;
        this.name = name;
        this.focusRate = focusRate;
        this.holdingChange = holdingChange;
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

    public int getFocusRate() {
        return focusRate;
    }

    public void setFocusRate(int focusRate) {
        this.focusRate = focusRate;
    }

    public int getHoldingChange() {
        return holdingChange;
    }

    public void setHoldingChange(int holdingChange) {
        this.holdingChange = holdingChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsolidationInfo that = (ConsolidationInfo) o;

        return Objects.equal(this.code, that.code) &&
                Objects.equal(this.name, that.name) &&
                Objects.equal(this.focusRate, that.focusRate) &&
                Objects.equal(this.holdingChange, that.holdingChange);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code, name, focusRate, holdingChange);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("code", code)
                .add("name", name)
                .add("focusRate", focusRate)
                .add("holdingChange", holdingChange)
                .toString();
    }
}
