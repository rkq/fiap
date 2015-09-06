package com.github.rkq.fiap.holding.model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Created by rick on 9/5/15.
 */
public class ExecutiveHoldingChange {
    private String stockCode;
    private String entityName;
    private String executiveName;
    private int holdingChange;
    private float averagePrice;
    private Date changeDate;
    private int holdingShare;

    public ExecutiveHoldingChange(String stockCode, String entityName, String executiveName, int holdingChange,
                                  float averagePrice, Date changeDate, int holdingShare) {
        this.stockCode = stockCode;
        this.entityName = entityName;
        this.executiveName = executiveName;
        this.holdingChange = holdingChange;
        this.averagePrice = averagePrice;
        this.changeDate = changeDate;
        this.holdingShare = holdingShare;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getExecutiveName() {
        return executiveName;
    }

    public void setExecutiveName(String executiveName) {
        this.executiveName = executiveName;
    }

    public int getHoldingChange() {
        return holdingChange;
    }

    public void setHoldingChange(int holdingChange) {
        this.holdingChange = holdingChange;
    }

    public float getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(float averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public int getHoldingShare() {
        return holdingShare;
    }

    public void setHoldingShare(int holdingShare) {
        this.holdingShare = holdingShare;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("stockCode", stockCode)
                .add("entityName", entityName)
                .add("executiveName", executiveName)
                .add("holdingChange", holdingChange)
                .add("averagePrice", averagePrice)
                .add("changeDate", changeDate)
                .add("holdingShare", holdingShare)
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExecutiveHoldingChange that = (ExecutiveHoldingChange) o;

        return Objects.equal(this.stockCode, that.stockCode) &&
                Objects.equal(this.entityName, that.entityName) &&
                Objects.equal(this.executiveName, that.executiveName) &&
                Objects.equal(this.holdingChange, that.holdingChange) &&
                Objects.equal(this.averagePrice, that.averagePrice) &&
                Objects.equal(this.changeDate, that.changeDate) &&
                Objects.equal(this.holdingShare, that.holdingShare);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stockCode, entityName, executiveName, holdingChange, averagePrice, changeDate,
                holdingShare);
    }
}
