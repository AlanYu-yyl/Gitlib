package com.humility.server;

import com.humility.datas.Good;

import java.util.stream.Stream;

public class GoodServiceImpl implements GoodService {

    //固定的端口号.
    public static final int PORT = 50004;

    @Override
    public Boolean releaseGood(Good good) {
        return null;
    }

    @Override
    public Stream<Good> getGoodsList(Integer userId) {
        return null;
    }

    @Override
    public Stream<Good> searchGoods(Integer userId) {
        return null;
    }
}
