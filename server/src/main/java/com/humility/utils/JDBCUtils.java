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

    private QueryRunner queryRunner;

    public JDBCUtils() {
        this.queryRunner = new QueryRunner();
    }

    public int insertUser(User user) {
        int line = 0;
        log.info("Inserting a user.");
        String sql = "INSERT INTO users(username, password, realname, phoneNumber, emailAddress, qqAccount) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = {user.getUsername(), user.getPassword(), user.getRealname(), user.getPhoneNumber(), user.getEmailAddress(), user.getQqAccount()};
        try (Connection con = Server.getDataSource().getConnection()) {
            line = queryRunner.update(con, sql, params);
        } catch (SQLException e) {
            log.error("数据库更新失败");
            //TODO 异常处理逻辑.
        }
        return line;
    }

    public User checkAccount(Account account) {
        User ret = null;
        log.info("Checking the account info.");
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        Object[] objects = null;
        try (Connection con = Server.getDataSource().getConnection()) {
            objects = queryRunner.query(con, sql, new ArrayHandler(), account.getUsername(), account.getPassword());
            if (objects.length > 0) {
                ret = new User();
                ret.setUser_id((Integer) objects[0]);
                ret.setUsername((String) objects[1]);
                ret.setPassword((Integer) objects[2]);
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

        if (ret == null)
            log.info("Invalid account.");
        else
            log.info("Valid account.");
        return ret;
    }
}