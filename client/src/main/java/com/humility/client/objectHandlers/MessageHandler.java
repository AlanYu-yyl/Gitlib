package com.humility.client.objectHandlers;

import com.humility.client.Client;
import com.humility.client.view.component.ChatRoom;
import com.humility.client.view.component.Session;
import com.humility.datas.Message;

/**
 * @author Humility <Yiling Yu>
 */

public class MessageHandler implements ObjectHandler {
  @Override
  public void handleObejct(Object obj, Client client) {
    Message message = (Message) obj;
    for (Session session : ChatRoom.sessions) {
      if (session.other_id == message.getSender_id()) {
        session.messages.add(message);
        session.refreshPanel();
        return;
      }
    }
  }
}
