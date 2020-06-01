package com.humility.client.interfaces;

import com.humility.datas.Good;

import java.util.ArrayList;

/**
 * 处理商品相关业务的服务对象
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月7日00:17:32
 */
public interface GoodService {

    /**
     * 用来发布商品的方法.
     * 传入用Good封装好的商品的信息,在数据库中注册一下.
     * @param good      用Good封装列商品的具体信息
     * @return
     */
    public Boolean releaseGood(Good good);

    /**
     * 获取商品列表.
     * 通过检索.
     * @return
     */
    public ArrayList<Good> getGoodsList();

    /**
     * 通过商品名来搜索商品.
     * @return
     */
    public ArrayList<Good> searchGoods(String name);
}
