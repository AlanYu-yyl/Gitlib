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
    public User login(Account account) {
        User result = null;
        result = jdbcUtils.checkAccount(account);
        if (result != null) {
            Server.getServer().setUserStatus(result.getUser_id(), Server.ClientStatus.Status.ONLINE);
        }
        return result;
    }

    /**
     * 用户注册
     * @param user 将用户的注册信息封装进User对象中传入.
     * @return
     */
    @Override
    public Boolean register(User user) {
        Boolean result = false;
        int line = jdbcUtils.insertUser(user);
        if (line > 0)
            result = true;
        String username = user.getUsername();
        Integer password = user.getPassword();
        User registeredUser = jdbcUtils.checkAccount(new Account(username, password));
        Server.getServer().addRegisteredUser(registeredUser.getUser_id());
        return result;
    }
}
