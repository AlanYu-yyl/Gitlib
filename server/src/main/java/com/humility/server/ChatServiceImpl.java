package com.humility.server;

import com.humility.datas.Message;

import java.util.List;

public class ChatServiceImpl implements ChatService {

    //固定的端口号.
    public static final int PORT = 50003;

    @Override
    public List<Message> getOffLineMessages(Integer userId) {
        return null;
    }

    @Override
    public void sendMessage(String message, Integer senderId, Integer getterId) {

    }
}
