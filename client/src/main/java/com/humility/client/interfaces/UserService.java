package com.humility.client.interfaces;

import com.humility.datas.Account;

/**
 * 处理用户登录和注册的服务接口
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月7日00:06:49.
 */
public interface UserService {

    //固定的端口号.
    public static final int PORT = 50001;

    /**
     * 简单的登录请求
     * 传入账号和密码,查询数据库并返回登录是否合法.
     * @param account 将账号和密码封装进Account对象中传入.
     * @return User 返回结果为登录传入账号的用户,若返回NULL则表示账号或密码错误.
     */
    public Integer login(Account account);

    /**
     * 简单的注册请求
     * 传入用User封装好的用户的注册信息,在数据库进行注册.返回注册是否成功.
     * @param user 将用户的注册信息封装进User对象中传入.
     * @return 返回结果为注册请求的成功与否.
     */
    public Boolean register(Account account);

}
