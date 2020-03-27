package com.humility.server;

import com.humility.datas.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {

    //固定的端口号.
    public static final int PORT = 50003;

    @Override
    public List<Message> getOffLineMessages(Integer userId) {
        List<Message> offlineMessages = new ArrayList<>();
        List<Object[]> resultSet = Server.jdbcUtils.queryAllOffLineMessages(userId);
        for (Object[] result : resultSet) {
            offlineMessages.add(new Message((String)result[0], (Integer)result[1]));
        }
        return offlineMessages;
    }

    @Override
    public void sendMessage(String message, Integer senderId, Integer getterId) {
        int messageId = Server.getServer().jdbcUtils.insertMessage(message, senderId, getterId);
        Message messageObj = new Message(message, senderId);
        if (!Server.getServer().sendObject(getterId, messageObj)) {
            Server.getServer().jdbcUtils.insertOffLineMessage(messageId, getterId);
        }
        return;
    }
}
