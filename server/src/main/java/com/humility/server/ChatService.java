package com.humility.server;

import com.humility.datas.Message;

import java.util.List;

/**
 * 处理聊天业务的服务对象
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月7日00:18:31
 */
public interface ChatService {

    /**
     * 客户端用来接收离线信息的方法.
     */
    public List<Message> getAllMessages(Integer uid);

    /**
     * &#x5BA2;&#x6237;&#x7AEF;&#x53D1;&#x9001;&#x4FE1;&#x606F;&#x7684;&#x65B9;&#x6CD5;.
     * @param message &#x53D1;&#x9001;&#x7684;&#x4FE1;&#x606F;
     * @return &#x65E0;&#x8FD4;&#x56DE;&#x503C;
     */
    public void sendMessage(Message message);
}
