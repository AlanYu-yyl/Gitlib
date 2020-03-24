package com.humility.utils;

import com.humility.datas.Account;
import com.humility.datas.User;
import com.humility.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class JDBCUtils {

    /**
     * 用于插入用户的详细信息 -- 在注册时调用.
     * @param user
     * @return  int line 返回更新行数.
     */
    public int insertUserInfo(User user) {
        int line = 0;
        log.info("Inserting a user.");
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
        String sqlInfo = "INSERT INTO users_info(realname, phoneNumber, emailAddress, qqAccount) VALUES (?, ?, ?, ?)";
        Object[] params = {user.getUsername(), user.getPassword()};
        Object[] paramsInfo = {user.getRealname(), user.getPhoneNumber(), user.getEmailAddress(), user.getQqAccount()};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            line = queryRunner.update(con, sql, params);
            queryRunner.update(con, sqlInfo, paramsInfo);
        } catch (SQLException e) {
            log.error("数据库更新失败");
            //TODO 异常处理逻辑.
        }
        return line;
    }

    /**
     * 通过账号信息来查询对应的用户.
     * @param account
     * @return  返回的账号对应的用户
     * @return null表示账号违法.
     */
    public User queryUser(Account account) {
        User ret = null;
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        Object[] objects = null;
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            objects = queryRunner.query(con, sql, new ArrayHandler(), account.getUsername(), account.getPassword());
            if (objects.length > 0) {
                ret = new User();
                ret.setUser_id((Integer) objects[0]);
                ret.setUsername((String) objects[1]);
                ret.setPassword((Integer) objects[2]);
            }
        } catch (SQLException e) {
            log.error("数据库查询失败");
            //TODO 查询失败的处理逻辑.
        }
        if (ret == null)
            log.info("Invalid account.");
        else
            log.info("Valid account.");
        return ret;
    }

    /**
     * 通过账号信息来查询用户的详细信息.
     * @param account
     * @return User 如果为null则表示账号违法.
     */
    public User queryUserInfo(Account account) {
        User ret = queryUserInfo(account);
        String sql = "SELECT * FROM users inner join users_info on " +
                "users.users_id=users_info.users_id WHERE users.users_id=?";
        Object[] objects = null;
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            objects = queryRunner.query(con, sql, new ArrayHandler(), ret.getUser_id());
            if (objects.length > 0) {
                if (objects[3] != null) {
                    ret.setRealname((String) objects[3]);
                }
                if (objects[4] != null) {
                    ret.setPhoneNumber((String) objects[4]);
                }
                if (objects[5] != null) {
                    ret.setEmailAddress((String) objects[5]);
                }
                if (objects[6] != null) {
                    ret.setQqAccount((String) objects[6]);
                }
            }
        } catch (SQLException e) {
            log.error("数据库查询失败");
            //TODO 查询失败的处理逻辑.
        }
        return ret;
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