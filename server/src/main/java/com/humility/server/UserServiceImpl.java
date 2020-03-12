package com.humility.server;

import com.humility.datas.Account;
import com.humility.datas.User;
import static com.humility.server.Server.jdbcUtils;

public class UserServiceImpl implements UserService {

    //固定的端口号
    public static final int PORT = 50001;
    @Override
    public User login(Account account) {
        User result = null;
        result = jdbcUtils.checkAccount(account);
        return result;
    }

    @Override
    public Boolean register(User user) {
        Boolean result = false;
        int line = jdbcUtils.insertUser(user);
        if (line > 0)
            result = true;
        return result;
    }
}
