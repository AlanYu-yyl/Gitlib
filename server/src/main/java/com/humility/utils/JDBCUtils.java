package com.humility.utils;

import com.humility.datas.Account;
import com.humility.datas.User;
import com.humility.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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
     * 插入用户发送的信息.
     * @param message
     * @param senderId
     * @param getterId
     * @return 返回消息id.
     */
    public int insertMessage(String message, Integer senderId, Integer getterId) {
        int messageId = 0;
        String insert = "INSERT INTO messages(message_content, message_time, sender_id, getter_id) VALUES (?, ?, ?, ?)";
        String query = "SELECT max(message_id) FROM messages";
        Object[] insert_params = {message, new Timestamp(System.currentTimeMillis()), senderId, getterId};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            queryRunner.update(con, insert, insert_params);
            messageId = queryRunner.query(con, query, new ScalarHandler<Integer>());
        } catch (SQLException e) {
            log.error("Fail to insert the message!");
        }
        return messageId;
    }

    /**
     * 插入离线消息.
     * @param messageId
     * @param getterId
     * @return
     */
    public int insertOffLineMessage(int messageId, Integer getterId) {
        int line = 0;
        String insert = "INSERT INTO offline_messages(message_id, getter_id) VALUE (?, ?)";
        Object[] params = {messageId, getterId};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            line = queryRunner.update(con, insert, params);
        } catch (SQLException e) {
            log.error("Fail to insert the offline message!");
        }
        return line;
    }

    /**
     * 查询某用户的离线消息.
     * @param getterId
     * @return
     */
    public List<Object[]> queryAllOffLineMessages(Integer getterId) {
        String sql = "SELECT message_content, sender_id FROM offline_messages a inner join messages b on a.message_id=b.message_id WHERE a.getter_id=?";
        Object[] param = {getterId};
        List<Object[]> offLineMessages = null;
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            offLineMessages = queryRunner.query(con, sql, new ArrayListHandler(), param);
        } catch (SQLException e) {
            log.error("Fail to get the offline messages of user " + getterId);
        }
        return offLineMessages;
    }

    /**
     * 删除某用户的离线消息.
     * @param userId
     * @return
     */
    public int deleteOffLineMessage(Integer userId) {
        int line = 0;
        String sql = "DELETE FROM offline_messages WHERE getter_id=?";
        Object[] param = {userId};
        try (Connection con = Server.getServer().getDataSource().getConnection()) {
            line = queryRunner.update(con, sql, param);
        } catch(SQLException e) {
            log.error("Fail to delete the offLineMessage of user " + userId);
        }
        return line;
    }

    /**
     * 用于在服务端启动时加载所有用户信息.
     * @return
     * @throws RuntimeException
     */
    public List<Object[]> queryAllUsers() throws RuntimeException {
        String sql = "SELECT user_id FROM users";
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