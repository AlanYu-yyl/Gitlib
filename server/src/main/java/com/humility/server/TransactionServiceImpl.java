package com.humility.server;


import com.humility.datas.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    //固定的端口号.
    public static final int PORT = 50002;

    @Override
    public Boolean makeComment(Transaction transaction) {
        Server.jdbcUtils.makeComment(transaction);
        return true;
    }

    @Override
    public void confirmTransaction(Transaction transaction) {
        Server.jdbcUtils.confirmTransaction(transaction);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        Server.jdbcUtils.insertTransaction(transaction);
    }

    @Override
    public List<Transaction> getCurrentTransaction(Integer uid) {
        List<Transaction> ret = new ArrayList<>();
        List<Object[]> objsList = Server.jdbcUtils.queryCurrentTransaction(uid);
        if (objsList != null) {
            objsList.forEach((Object[] objs) -> {
                ret.add(resolveTransaction(objs));
            });
        }
        return ret;
    }

    public void confirmTransactionInfo(Transaction transaction) {
        Server.jdbcUtils.updateTransactionInfo(transaction);
    }

    private Transaction resolveTransaction(Object[] objs) {
        Transaction ret = new Transaction();
        ret.setTid((Integer) objs[0]);
        ret.setGid((Integer) objs[1]);
        ret.setBuyer_id((Integer) objs[2]);
        ret.setSeller_id((Integer) objs[3]);
        ret.setTprice((BigDecimal) objs[5]);
        ret.setTimeMillis((Long) objs[6]);
        return ret;
    }
}
