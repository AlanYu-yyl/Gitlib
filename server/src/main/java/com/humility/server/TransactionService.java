package com.humility.server;

import com.humility.datas.Transaction;

import java.util.List;

/**
 * 处理交易的服务接口
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月7日00:07:36
 */
public interface TransactionService {

    public static final int port = 50002;

    /**
     * 评价,由用户对商品进行评价.
     * 传入评价者id,商品id和评价信息.在该商品的评价区发布该评价.
     * @param userId            评价者id
     * @param goodId            被评价的商品id
     * @param commentMessage    评价信息
     * @return 返回是否评价成功.
     */
    public Boolean makeComment(Integer userId, Integer goodId, String commentMessage);

    public void createTransaction(Transaction transaction);

    public void confirmTransaction(Transaction transaction);

    public List<Transaction> getCurrentTransaction(Integer uid);
}

