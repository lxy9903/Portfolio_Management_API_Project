package com.portfolio.mgmtsys.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portfolio.mgmtsys.domain.Account;
import com.portfolio.mgmtsys.service.AccountService;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    
    @Autowired
    AccountService service;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Account account){
        Integer accountId = service.login(account);
        if(accountId!=null){
            Map<String, Object> body = new HashMap<>();
            body.put("id", accountId);
            body.put("name", account.getName());
            return new ResponseEntity<Object>(body, HttpStatus.OK);
        }
        return new ResponseEntity<Object>("Account name or password may be incorrect.", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Account account){
        Account registeredAccount = service.register(account);
        if(registeredAccount!=null){
            return new ResponseEntity<Object>(registeredAccount, HttpStatus.OK);
        }
        return new ResponseEntity<Object>("Account name may already exist.", HttpStatus.CONFLICT);
    }
}
