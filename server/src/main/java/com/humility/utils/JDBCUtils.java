package com.humility.utils;

import com.humility.datas.*;
import com.humility.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class JDBCUtils {

    /**
     * 用于插入用户的信息 -- 在注册时调用.
     * @param account
     * @return  int line 返回更新行数.
     */
    public int insertUser(Account account) {
        int line = 0;
        log.info("Inserting a user.");
        String sql = "INSERT INTO user(username, password) VALUES (?, ?)";
        Object[] params = {account.getUsername(), account.getPassword()};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            line = queryRunner.update(con, sql, params);
        } catch (SQLException e) {
            log.error("数据库更新失败");
            //TODO 异常处理逻辑.
        }
        return line;
    }

    /**
     * 通过用户id来查询对应的用户.
     * @param uid
     * @return  返回uid对应的用户
     * @return null表示账号违法.
     */
    public User queryUser(int uid) {
        User ret = null;
        String sql = "SELECT * FROM user WHERE uid=?";
        Object[] res = null;
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            res = queryRunner.query(con, sql, new ArrayHandler(), uid);
            if (res.length > 0) {
                ret = new User();
                ret.setUid((Integer) res[0]);
                ret.setUsername((String) res[1]);
                ret.setPassword((Integer) res[2]);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败");
            //TODO 查询失败的处理逻辑.
        }
        if (ret == null)
            log.info("Invalid uid.");
        else
            log.info("Valid uid.");
        return ret;
    }

    /**
     * 通过账号来查询对应的uid.
     * @param account
     * @return 返回账号对应的uid.
     */
    public int queryUid(Account account) {
        int ret = -1;
        String sql = "SELECT uid FROM user WHERE username=? AND password=?";
        Object[] res = null;
        try(Connection con = Server.getServer().getDataSource().getConnection()) {
            res = queryRunner.query(con, sql, new ArrayHandler(), account.getUsername(), account.getPassword());
            if (res.length > 0) {
                ret = (int)res[0];
            }
        } catch (SQLException e) {
            log.error("数据库查询失败");
            //TODO 处理逻辑.
        }
        if (ret == -1)
            log.info("Invalid account.");
        else
            log.info("Valid account.");
        return ret;
    }

    /**
     * 用于在服务端启动时加载所有用户信息.
     * @return
     * @throws RuntimeException
     */
    public List<Object[]> queryAllUsers() throws RuntimeException {
        String sql = "SELECT uid FROM user";
        List<Object[]> userArray = null;
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            userArray = queryRunner.query(con, sql, new ArrayListHandler());
        } catch (SQLException e) {
            String info = "Fail to load the info of users!";
            log.error(info);
            throw new RuntimeException(info ,e);
        }
        return userArray;
    }

    public int insertGood(Good good) {
        int ret = 0;
        String sql = "INSERT INTO good(gname, owner, price, image, description, is_selled)" +
                "VALUES(?, ?, ?, ?, ?,?)";
        byte[] imageBuffer = new byte[0];
        try {
            imageBuffer = getImageBuffer(good.getImage());
        } catch (IOException e) {
            log.info("图片-二进制转化失败");
            //TODO 异常处理逻辑.
        }
        Object[] params = { good.getGname(), good.getOwner(), good.getPrice(),imageBuffer
            , good.getDescription(), good.getIs_selled() };
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.update(con, sql, params);
            log.info("商品注册成功");
        } catch (SQLException e) {
            log.info("商品注册失败");
            //TODO 异常处理逻辑.
        }
        return ret;
    }

    public List<Object[]> queryGood(int gid) {
        List<Object[]> ret = null;
        String sql = "SELECT * FROM good WHERE gid=?";
        Object[] params = {(Integer) gid};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.query(con, sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            log.info("商品查询失败");
            //TODO 异常处理逻辑.
        }
        return ret;
    }

    /**
     * 查询所有的good对象.
     * @return
     */
    public List<Object[]> queryAllGoods() {
        List<Object[]> ret = null;
        String sql = "SELECT * FROM good WHERE is_selled=0";
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.query(con, sql, new ArrayListHandler());
        } catch (SQLException e) {
            log.info("查询失败");
            //TODO 异常处理逻辑.
        }
        return ret;
    }

    public int insertMessage(Message message) {
        int ret = 0;
        String sql = "INSERT INTO message(message, sender_id, getter_id, mtime, is_received)" +
                "VALUES(?, ?, ?, ?, ?)";
        Object[] params = {message.getMessage(), message.getSender_id(), message.getGetter_id(), System.currentTimeMillis(), false};
        try(Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.update(con, sql, params);
        } catch (SQLException e) {
            log.info("消息插入失败, 请检查日期格式.");
            //TODO 异常处理逻辑.
        }
        return ret;
    }

    public int receivedMessage(Message message) {
        int ret = 0;
        String sql = "UPDATE message SET is_received 1 WHERE message=? AND sender_id=? AND getter_id=?";
        Object[] params = { message.getMessage(), message.getSender_id(), message.getGetter_id() };
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.update(con, sql, params);
        } catch (SQLException e) {
            log.info("信息状态更改失败.");
        }
        return ret;
    }

    public List<Object[]> queryAllMessages() {
        List<Object[]> ret = null;
        String sql = "SELECT * FROM message";
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.query(con, sql, new ArrayListHandler());
        } catch (SQLException e) {
            log.info("消息获取失败.");
        }
        return ret;
    }

    public int insertTransaction(Transaction transaction) {
        int ret = 0;
        String sql = "INSERT INTO transaction(gid, buyer_id, seller_id," +
                "tprice, ttime, is_selled) VALUES(?, ?, ?, ?, ?, ?)";
        Object[] params = {transaction.getGid(), transaction.getBuyer_id(),
        transaction.getSeller_id(), (BigDecimal) queryGood(transaction.getGid()).get(0)[3],
        transaction.getTimeMillis(), false};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.update(con, sql, params);
        } catch(SQLException e) {
            log.info("交易插入失败");
        }
        return ret;
    }

    public int confirmTransaction(Transaction transaction) {
        int ret = 0;
        String sql = "UPDATE transaction SET tprice=?, is_selled=?";
        Object[] params = {transaction.getTprice(), true};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            ret = queryRunner.update(con, sql, params);
        } catch (SQLException e) {
            log.info("交易确认失败");
        }
        return ret;
    }

    /**
     * 将图片转换成二进制.
     * @param imageIcon
     * @return
     */
    private byte[] getImageBuffer(ImageIcon imageIcon) throws IOException {
        Image image = imageIcon.getImage();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //用于封装数据库查询的对象.
    private QueryRunner queryRunner;

    /**
     * 简单的无参初始化器.
     */
    public JDBCUtils() {
        this.queryRunner = new QueryRunner();
    }
}