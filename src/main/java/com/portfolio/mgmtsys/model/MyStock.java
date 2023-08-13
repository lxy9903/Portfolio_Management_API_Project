package com.portfolio.mgmtsys.model;

/*
 * Name: xiaoyu
 * Date: 2023/8/13
 * Description:
 */

public class MyStock {
    String stockName;

    String ticker;

    Integer amount;

    Double currentPrice;

    public MyStock(String stockName, String ticker, Integer amount, Double currentPrice) {
        this.stockName = stockName;
        this.ticker = ticker;
        this.amount = amount;
        this.currentPrice = currentPrice;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }
}