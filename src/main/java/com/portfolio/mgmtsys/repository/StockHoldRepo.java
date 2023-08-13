package com.portfolio.mgmtsys.repository;

/*
 * Name: xiaoyu
 * Date: 2023/8/13
 * Description:
 */

import com.portfolio.mgmtsys.domain.StockHold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface StockHoldRepo extends JpaRepository<StockHold, Integer> {
    @Query("SELECT e FROM StockHold e WHERE e.accountId = :accountId")
    ArrayList<StockHold> findAllByAccountId(Integer accountId);
}