package com.humility.client.objectHandlers;

import com.humility.client.Client;
import com.humility.client.view.component.ChatRoom;
import com.humility.datas.Good;
import com.humility.datas.Transaction;

import javax.swing.*;

/**
 * @author Humility <Yiling Yu>
 */

public class TransactionHandler implements ObjectHandler{
  @Override
  public void handleObejct(Object obj, Client client) {
    Transaction transaction = (Transaction) obj;
    if (transaction.getTprice() == null) {
      ChatRoom.getSession(transaction);
    }
    if (transaction.getTprice() != null) {
      Good good = Client.getClient().getGoodService().searchGood(transaction.getGid());
      int result = JOptionPane.showConfirmDialog(ChatRoom.currentSession,
              good.getGname()+"的最终报价为"+transaction.getTprice(),
              "交易确认", JOptionPane.YES_NO_OPTION);
      if (result == 1) {
        Client.getClient().getTransactionService().confirmTransaction(transaction);
        JOptionPane.showMessageDialog(ChatRoom.currentSession, "交易确认, 请在线下自行结账.");
      }
    }
  }
}
