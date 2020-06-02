package com.humility.server;

import com.humility.datas.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatService {

    //固定的端口号.
    public static final int PORT = 50003;

    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        List<Object[]> tmp = Server.jdbcUtils.queryAllMessages();
        tmp.forEach((Object[] objects) -> {
            messages.add(resolveMessage(objects));
        });
        return messages;
    }

    @Override
    public void sendMessage(Message message) {
        Server.jdbcUtils.insertMessage(message);
        Server.getServer().sendObject(message.getGetter_id(), message);
        Server.jdbcUtils.receivedMessage(message);
    }

    private Message resolveMessage(Object[] objs) {
        Message ret = null;
        ret.setMid((Integer)objs[0]);
        ret.setMessage((String) objs[1]);
        ret.setSender_id((Integer) objs[2]);
        ret.setGetter_id(((Integer) objs[3]));
        ret.setTimeMillis((Long) objs[4]);
        ret.setIs_received((Boolean) objs[5]);
        return ret;
    }
}
