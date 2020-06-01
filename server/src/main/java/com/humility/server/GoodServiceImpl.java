package com.humility.server;

import com.humility.datas.Good;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodServiceImpl implements GoodService {

    //固定的端口号.
    public static final int PORT = 50004;

    @Override
    public Boolean releaseGood(Good good) {
        //TODO 实现发布商品
        int lines = Server.jdbcUtils.insertGood(good);
        if (lines == 0) return false;
        else return true;
    }

    @Override
    public ArrayList<Good> getGoodsList() {
        ArrayList<Good> ret = new ArrayList<Good>();
        List<Object[]> goodList = Server.jdbcUtils.queryAllGoods();
        goodList.forEach((objects) -> {

        });
    }

    @Override
    public ArrayList<Good> searchGoods(String name) {
        return null;
    }

    /**
     * 解析商品对象的查询结果.
     * @param objects
     * @return
     */
    private Good resolveGood(Object[] objects) {
        Good ret = new Good();
        ret.setGid((Integer)objects[0]);
        ret.setGname((String)objects[1]);
        ret.setOwner((Integer) objects[2]);
        ret.setPrice((BigDecimal)objects[3]);
        try {
            ret.setImage(resolveImage((byte[]) objects[4]));
        } catch (IOException e) {
            //TODO 处理逻辑
        }
        ret.setDescription((String) objects[5]);
        ret.setIs_selled(false);
        return ret;
    }

    /**
     * 解析图片
     * @param bytes
     * @return
     */
    private ImageIcon resolveImage(byte[] bytes) throws IOException {
        InputStream buffin = new ByteArrayInputStream(bytes, 0, bytes.length);
        BufferedImage bi = ImageIO.read(buffin);
        return new ImageIcon(bi);
    }
}
