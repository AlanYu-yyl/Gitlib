package com.humility.client.view.component;

import com.humility.client.Client;
import com.humility.datas.Good;
import com.humility.datas.Message;
import com.humility.datas.Transaction;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Humility <Yiling Yu>
 */

public class ChatRoom extends javax.swing.JPanel {

  /**
   * Creates new form ChatRoom1
   */
  public ChatRoom() {
    initComponents();
  }

  private void initComponents() {

    session_scroll = new javax.swing.JScrollPane();
    session_panel = new javax.swing.JPanel();
    input_scroll = new javax.swing.JScrollPane();
    input_area = new javax.swing.JTextArea();
    send_bt = new javax.swing.JButton();
    deal_bt = new javax.swing.JButton();
    show_scroll = new javax.swing.JScrollPane();
    show_panel = new javax.swing.JLayeredPane();

    Client.getClient().getTransactionService().getCurrentTransaction(Client.getClient().getMe())
            .forEach((Transaction transaction) -> getSession(transaction));
    Client.getClient().getChatService().getAllMessages(Client.getClient().getMe())
            .forEach((Message message) -> {
              for (Session session : sessions) {
                if (Client.getClient().getMe() == message.getGetter_id()) {
                  if (session.other_id == message.getSender_id()) {
                    session.messages.add(message);
                  }
                } else {
                  if (session.other_id == message.getGetter_id()) {
                    session.messages.add(message);
                  }
                }
              }
            });

    setBounds(0, 0, 953, 680);

    session_scroll.setPreferredSize(new java.awt.Dimension(264, 768));

    session_panel.setPreferredSize(new java.awt.Dimension(264, 680));

    javax.swing.GroupLayout session_panelLayout = new javax.swing.GroupLayout(session_panel);
    session_panel.setLayout(session_panelLayout);
    session_panelLayout.setHorizontalGroup(
            session_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 264, Short.MAX_VALUE)
    );
    session_panelLayout.setVerticalGroup(
            session_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 680, Short.MAX_VALUE)
    );

    session_scroll.setViewportView(session_panel);

    input_area.setColumns(20);
    input_area.setRows(5);
    input_scroll.setViewportView(input_area);

    send_bt.setFont(new java.awt.Font("幼圆", 1, 18)); // NOI18N
    send_bt.setText("发   送");
    send_bt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        send_btActionPerformed(evt);
      }
    });

    deal_bt.setFont(new java.awt.Font("幼圆", 1, 18)); // NOI18N
    deal_bt.setText("成   交");
    deal_bt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deal_btActionPerformed(evt);
      }
    });

    show_scroll.setPreferredSize(new java.awt.Dimension(612, 600));
    show_scroll.setRequestFocusEnabled(false);

    show_panel.setPreferredSize(new java.awt.Dimension(612, 540));

    javax.swing.GroupLayout show_panelLayout = new javax.swing.GroupLayout(show_panel);
    show_panel.setLayout(show_panelLayout);
    show_panelLayout.setHorizontalGroup(
            show_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 612, Short.MAX_VALUE)
    );
    show_panelLayout.setVerticalGroup(
            show_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 540, Short.MAX_VALUE)
    );

    show_scroll.setViewportView(show_panel);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(session_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(deal_bt, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(38, 38, 38)
                                            .addComponent(send_bt, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(input_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(show_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(47, 47, 47))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(session_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(show_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(input_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(deal_bt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(send_bt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
    );
  }// </editor-fold>

  private void deal_btActionPerformed(java.awt.event.ActionEvent evt) {
    if (Client.getClient().getMe() == currentSession.good.getOwner()) {
      Transaction transaction = new Transaction();
      transaction.setGid(currentSession.good.getGid());
      transaction.setBuyer_id(currentSession.other_id);
      transaction.setSeller_id(Client.getClient().getMe());
      transaction.setTimeMillis(System.currentTimeMillis());
      String input_price = null;
      input_price = JOptionPane.showInputDialog("请输入最终报价(￥).");
      transaction.setTprice(new BigDecimal(input_price));
      Client.getClient().getTransactionService().confirmTransactionInfo(transaction);
    }
    else {
      JOptionPane.showMessageDialog(this, "请等候卖家确定价格.");
    }
  }

  private void send_btActionPerformed(java.awt.event.ActionEvent evt) {
    if (currentSession == null) return;
    Message message = new Message();
    message.setMessage(input_area.getText());
    input_area.setText("");
    message.setSender_id(Client.getClient().user.getUid());
    message.setGetter_id(currentSession.other_id);
    message.setTimeMillis(System.currentTimeMillis());
    message.setIs_received(false);
    currentSession.messages.add(message);
    currentSession.refreshPanel();
    Client.getClient().getChatService().sendMessage(message);
  }

  public static void createSession(Good good) {
    Session session = new Session(good, Client.getClient().getMe());
    sessions.add(session);
    paintSessionPanel();
    getFocus(session);
    currentSession = session;
  }

  public static void getSession(Transaction transaction) {
    Session session = new Session(Client.getClient().getGoodService().searchGood(transaction.getGid()),
            transaction.getBuyer_id());
    sessions.add(session);
    paintSessionPanel();
    getFocus(session);
    currentSession = session;
  }

  public static void paintSessionPanel() {
    session_panel.removeAll();
    for (int i = 0; i < sessions.size(); i++) {
      session_panel.add(sessions.get(i));
      sessions.get(i).setBounds(0, i * 66, 256, 64);
    }
    session_panel.repaint();
  }

  public static void getFocus(Session session) {
    ChatRoom.sessions.forEach((Session sess) -> {
      sess.panel.setVisible(false);
    });
    session.setVisible(true);
  }

  // Variables declaration - do not modify
  private javax.swing.JButton deal_bt;
  private javax.swing.JTextArea input_area;
  private javax.swing.JScrollPane input_scroll;
  private javax.swing.JButton send_bt;
  private static javax.swing.JPanel session_panel;
  private javax.swing.JScrollPane session_scroll;
  public static javax.swing.JLayeredPane show_panel;
  private javax.swing.JScrollPane show_scroll;
  public static List<Session> sessions = new ArrayList<>();
  public static Session currentSession = null;
  // End of variables declaration
}

