package com.humility.server.objectHandler;

import com.humility.datas.KeepAlive;
import com.humility.datas.Transaction;
import com.humility.server.Server;

/**
 * @author Humility <Yiling Yu>
 */

public class TransactionHandler implements ObjectHandler{
  @Override
  public Object handleObject(Object rev) {
    Transaction transaction = (Transaction) rev;
    Server.getServer().sendObject(transaction.getSeller_id(), transaction);
    return new KeepAlive();
  }
}
