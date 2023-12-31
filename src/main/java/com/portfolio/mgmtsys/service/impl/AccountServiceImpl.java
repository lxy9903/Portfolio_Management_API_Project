package com.portfolio.mgmtsys.service.impl;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.portfolio.mgmtsys.domain.Account;
import com.portfolio.mgmtsys.domain.Assets;
import com.portfolio.mgmtsys.repository.AccountRepo;
import com.portfolio.mgmtsys.repository.AssetsRepo;
import com.portfolio.mgmtsys.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepo repo;

    @Autowired
    AssetsRepo assetsRepo;

    public Integer login(Account account){
        Account accountEncrypted = new Account();
        accountEncrypted.setName(account.getName());
        accountEncrypted.setPassword(DigestUtils.md5Hex(account.getPassword()));
        Example<Account> nameAndPwd = Example.of(accountEncrypted);
        Optional<Account> verifiedAccount = repo.findOne(nameAndPwd);
        if(verifiedAccount.isPresent()){
            return verifiedAccount.get().getId();
        }
        return null;
    }

    public Account register(Account account){
        Account accountToBeRegistered = new Account();
        accountToBeRegistered.setName(account.getName());
        Example<Account> nameToBeRegistered = Example.of(accountToBeRegistered);
        Optional<Account> foundAccount = repo.findOne(nameToBeRegistered);
        if(foundAccount.isPresent()){
            return null;
        }
        Account accountEncrypted = new Account();
        accountEncrypted.setName(account.getName());
        accountEncrypted.setPassword(DigestUtils.md5Hex(account.getPassword()));
        Account save = repo.save(accountEncrypted);
        Account registeredAccount = repo.findOne(nameToBeRegistered).get();
//        registeredAccount.setPassword(null);
        Assets newAssets = new Assets();
        newAssets.setId(registeredAccount.getId());
        newAssets.setBalance(0.0);
        newAssets.setStockAssets(0.0);
        newAssets.setTotalAssets(0.0);
        assetsRepo.save(newAssets);
        return registeredAccount;
    }
}
