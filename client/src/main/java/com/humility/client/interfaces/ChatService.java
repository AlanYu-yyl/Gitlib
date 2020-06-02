package com.humility.client.interfaces;

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
     * @param userId 接受者的用户id用于map到对应的socket上
     * @return 返回一个离线消息的线性表.
     */
    public List<Message> getOffLineMessages(Integer userId);

    /**
     * 客户端发送信息的方法.
     * @param message 发送的信息
     * @param senderId 消息的发送者id
     * @param getterId 消息的接受者id
     * @return 无返回值
     */
    public void sendMessage(Message message);
}
