package com.portfolio.mgmtsys.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portfolio.mgmtsys.domain.Assets;
import com.portfolio.mgmtsys.domain.Transfer;
import com.portfolio.mgmtsys.enumeration.TransferType;
import com.portfolio.mgmtsys.repository.AssetsRepo;
import com.portfolio.mgmtsys.repository.TransferRepo;
import com.portfolio.mgmtsys.service.AssetsService;

@Service
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    AssetsRepo repo;

    @Autowired
    TransferRepo transferRepo;

    @Override
    public Assets getAssetsById(Integer id) {
        Optional<Assets> assetsOptional = repo.findById(id);
        if(assetsOptional.isPresent()){
            return assetsOptional.get();
        }
        return null;
    }

    @Override
    public Assets transferIn(Map<String, Object> request) {
        Integer id = (Integer)request.get("id");
        Double amount = (Double)request.get("amount");
        Optional<Assets> assetsOptional = repo.findById(id);
        if(!assetsOptional.isPresent()){
            return null;
        }
        Assets assets = assetsOptional.get();
        assets.setBalance(assets.getBalance() + amount);
        assets.setTotalAssets(assets.getBalance()+assets.getStockAssets()+assets.getFundAssets());
        repo.save(assets);

        Transfer transfer = new Transfer();
        transfer.setAccountId(id);
        transfer.setAmount(amount);
        transfer.setType(TransferType.IN);
        transfer.setTime(new Date());
        transferRepo.save(transfer);

        return assets;
    }

    @Override
    public Assets transferOut(Map<String, Object> request) {
        Integer id = (Integer)request.get("id");
        Double amount = (Double)request.get("amount");
        Optional<Assets> assetsOptional = repo.findById(id);
        if(!assetsOptional.isPresent()){
            return null;
        }
        Assets assets = assetsOptional.get();
        assets.setBalance(assets.getBalance() - amount);
        assets.setTotalAssets(assets.getBalance()+assets.getStockAssets()+assets.getFundAssets());
        repo.save(assets);

        Transfer transfer = new Transfer();
        transfer.setAccountId(id);
        transfer.setAmount(amount);
        transfer.setType(TransferType.OUT);
        transfer.setTime(new Date());
        transferRepo.save(transfer);

        return assets;
    }
    
}
