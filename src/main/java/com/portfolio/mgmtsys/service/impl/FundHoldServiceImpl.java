package com.portfolio.mgmtsys.service.impl;

/*
 * Name: xiaoyu
 * Date: 2023/8/16
 * Description:
 */

import com.portfolio.mgmtsys.domain.*;
import com.portfolio.mgmtsys.enumeration.FundTradeType;
import com.portfolio.mgmtsys.enumeration.TradeType;
import com.portfolio.mgmtsys.model.BuyAndSellFundRequest;
import com.portfolio.mgmtsys.repository.AssetsRepo;
import com.portfolio.mgmtsys.repository.FundHoldRepo;
import com.portfolio.mgmtsys.repository.FundRepo;
import com.portfolio.mgmtsys.repository.FundTradeRepo;
import com.portfolio.mgmtsys.service.FundHoldService;
import com.portfolio.mgmtsys.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FundHoldServiceImpl implements FundHoldService {
    @Autowired
    FundHoldRepo fundHoldRepo;

    @Autowired
    AssetsRepo assetsRepo;

    @Autowired
    FundRepo fundRepo;

    @Autowired
    FundTradeRepo fundTradeRepo;


    /**
     * 查询所有持有基金：/fundhold/getallfubdhold
     * 请求方式：get
     * @param accountId 登陆ID
     * @return [fundhold]
     */
    @Override
    public List<FundHold> getAllFundHold(Integer accountId) {
        // 1. 通过accountID查找所有FundHold
        List<FundHold> fundHolds = fundHoldRepo.findAllByAccountId(accountId);
        return fundHolds;
    }

    /**
     * 购买基金：/fundhold/buyfund
     * 请求方式：post
     * Response：{true/false}
     * @param request 登陆ID，基金代码，购买数目
     * @return {true/false}
     */
    @Override
    public boolean buyFund(BuyAndSellFundRequest request) {
        // 1. 通过登陆ID查找资产信息
        Assets asset = assetsRepo.findById(request.getAccountId()).orElse(null);
        if(asset == null){
            return false;
        }

        // 2. 通过基金代码查找基金信息
        Example<Fund> example = Example.of(new Fund(request.getCode()));
        Fund fund = fundRepo.findOne(example).orElse(null);
        if (fund == null){
            return false;
        }

        // 3. 新建/获取fundhold，通过code和accountId
        Example<FundHold> fundHoldExample = Example.of(new FundHold(request.getAccountId(), request.getCode()));
        FundHold fundHold = fundHoldRepo.findOne(fundHoldExample).orElse(null);
        if (fundHold == null){
            fundHold = new FundHold(request.getAccountId(), request.getCode());
            fundHold.setAmount(request.getAmount());
            fundHold.setSubscriptionPrice(fund.getUnitNet());
        }

        // 4. 判断能否购买
        Double now = asset.getBalance() - request.getAmount() * fund.getUnitNet();
        if (now < 0){
            return false;
        }

        // 5. 修改fundTrade、fundhold和assets表
        FundTrade fundTrade = new FundTrade(request.getAccountId(), request.getCode(),
                FundTradeType.SUBSCRIBE, TimeUtil.getNowTime(),fund.getUnitNet(), request.getAmount());

        fundHold.setAmount(fundHold.getAmount() + request.getAmount());
        asset.setBalance(now);

        // 6. 存入数据库
        fundTradeRepo.save(fundTrade);
        fundHoldRepo.save(fundHold);
        assetsRepo.save(asset);

        return true;
    }

    @Override
    public boolean sellFund(BuyAndSellFundRequest request) {
        if (request == null
                || request.getAccountId() == null
                || request.getAmount() == null
                || request.getCode() == null
                || request.getAmount() <= 0
        ){
            return false;
        }
        // 1. 获取账户资金
        Assets asset = assetsRepo.findById(request.getAccountId()).orElse(null);
        if (asset == null){
            return false;
        }

        // 2. 获取基金价格
        Fund fund = fundRepo.findById(request.getCode()).orElse(null);

        if(fund == null){
            return false;
        }
        Double currentPrice = fund.getUnitNet();

        // 3. 查找fundHold表
        FundHold fundHold = fundHoldRepo.findOne(Example.of(new FundHold(request.getAccountId(), request.getCode()))).orElse(null);
        if (fundHold == null){
            return false;
        }

        // 4. 判断是否可以售卖
        if (fundHold.getAmount() < request.getAmount()){
            return false;
        }

        // 5. 修改tradde表
        Date time = TimeUtil.getNowTime();
        FundTrade trade = new FundTrade(request.getAccountId(), request.getCode(),
                FundTradeType.REDEEM, time, currentPrice, request.getAmount());
        // 6. 修改StockHold表和Assets表
        fundHold.setAmount(fundHold.getAmount() - request.getAmount());
        Double now = asset.getBalance() + currentPrice * request.getAmount();
        asset.setBalance(now);
        fundTradeRepo.save(trade);
        assetsRepo.save(asset);
        fundHoldRepo.save(fundHold);
        return true;
    }
}