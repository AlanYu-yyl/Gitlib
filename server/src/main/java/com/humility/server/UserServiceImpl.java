package com.humility.server;

import com.humility.datas.Account;
import com.humility.datas.User;

import static com.humility.server.Server.jdbcUtils;

public class UserServiceImpl implements UserService {

    //固定的端口号
    public static final int PORT = 50001;

    /**
     * 用户登录
     * @param account 将账号和密码封装进Account对象中传入.
     * @return
     */
    @Override
    public Integer login(Account account) {
        Integer result = null;
        result = jdbcUtils.queryUid(account);
        if (result != null) {
            Server.getServer().setUserStatus(result, Server.ClientStatus.Status.ONLINE);
        }
        return result;
    }

    /**
     * 用户注册
     * @param account 将用户的注册信息封装进User对象中传入.
     * @return
     */
    @Override
    public Boolean register(Account account) {
        Boolean result = false;
        int line = jdbcUtils.insertUser(account);
        if (line > 0) {
            result = true;
            Server.getServer().addRegisteredUser(jdbcUtils.queryUid(account));
        }
        return result;
    }

    @Override
    public User getUserByUid(Integer uid) {
        return jdbcUtils.queryUser(uid);
    }
}
