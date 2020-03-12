package com.humility.server;


import com.humility.datas.Good;
import com.humility.datas.Transaction;
import com.humility.datas.User;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class TransactionServiceImpl implements TransactionService {

    //固定的端口号.
    public static final int PORT = 50002;

    @Override
    public Boolean createTransaction(Integer buyerId, Integer goodId) {
        return null;
    }

    @Override
    public Boolean auctionGood(Integer goodId, BigDecimal money) {
        return null;
    }

    @Override
    public Boolean makeComment(Integer userId, Integer goodId, String commentMessage) {
        return null;
    }

    @Override
    public Stream<Transaction> getMySalesStatus(Integer userId) {
        return null;
    }
}
