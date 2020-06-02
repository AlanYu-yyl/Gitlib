package com.humility.server;


import com.humility.datas.Transaction;

public class TransactionServiceImpl implements TransactionService {

    //固定的端口号.
    public static final int PORT = 50002;

    @Override
    public Boolean makeComment(Integer userId, Integer goodId, String commentMessage) {
        return null;
    }

    @Override
    public void confirmTransaction(Transaction transaction) {
        Server.jdbcUtils.confirmTransaction(transaction);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        Server.jdbcUtils.insertTransaction(transaction);
    }
}
