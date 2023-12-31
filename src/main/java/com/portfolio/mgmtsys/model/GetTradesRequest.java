package com.portfolio.mgmtsys.model;

/*
 * Name: xiaoyu
 * Date: 2023/8/15
 * Description:
 */

import java.util.Date;

public class GetTradesRequest extends TimeRequest {
   //    登陆ID，交易时间段（默认近七天）
   private Integer accountId;

   public GetTradesRequest(Integer accountId, Date startTime, Date endTime) {

      super(startTime, endTime);
      this.accountId = accountId;
   }

   public GetTradesRequest(){

   }

    public GetTradesRequest(Integer accountId, String startTime, String endTime) {

        super(startTime, endTime);
        this.accountId = accountId;
    }

    public Integer getAccountId() {
      return accountId;
   }

   public void setAccountId(Integer accountId) {
      this.accountId = accountId;
   }

   @Override
   public String toString() {
      return "GetTradesRequest{" +
              "accountId=" + accountId +
              ", startTime=" + startTime +
              ", endTime=" + endTime +
              '}';
   }
}

