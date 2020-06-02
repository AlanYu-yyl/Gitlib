package com.humility.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.humility.client.Client;
import com.humility.datas.Account;

import javax.swing.*;

/**
 * @author Humility <Yiling Yu>
 */

public class Register extends javax.swing.JFrame {

    /**
     * Creates new form Register
     */
    public Register() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        register_font = new javax.swing.JLabel();
        separator_top = new javax.swing.JSeparator();
        app_intro = new javax.swing.JLabel();
        username_label = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        password_label = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        separator_buttom = new javax.swing.JSeparator();
        author = new javax.swing.JLabel();
        hand_in = new javax.swing.JButton();
        identify_paswd = new javax.swing.JLabel();
        password_identify = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(501, 550));

        register_font.setFont(new java.awt.Font("宋体", 1, 48)); // NOI18N
        register_font.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        register_font.setText("注册");
        register_font.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        app_intro.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        app_intro.setText("java课程设计:二手交易平台");

        username_label.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        username_label.setText("用 户 名:");
        username_label.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        username.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        password_label.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        password_label.setText("密    码:");
        password_label.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        password.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        author.setFont(new java.awt.Font("微软雅黑 Light", 0, 12)); // NOI18N
        author.setText("Author: Humility_YilingYu");

        hand_in.setFont(new java.awt.Font("幼圆", 1, 24)); // NOI18N
        hand_in.setText("提      交");
        hand_in.setActionCommand("提               交");
        hand_in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hand_inActionPerformed(evt);
            }
        });

        identify_paswd.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        identify_paswd.setText("确认密码:");
        identify_paswd.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        password_identify.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N
        password_identify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                password_identifyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(separator_top, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(70, 70, 70)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                .addComponent(password, javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(password_label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(username_label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(username, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(identify_paswd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(password_identify, javax.swing.GroupLayout.Alignment.LEADING))))
                                                .addGap(71, 71, 71))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(app_intro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(register_font, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(hand_in, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(71, 71, 71))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(separator_buttom, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(author))
                                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(register_font, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(app_intro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(separator_top, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(username_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(password_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(identify_paswd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(password_identify, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(hand_in, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(separator_buttom, javax.swing.GroupLayout.DEFAULT_SIZE, 6, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(author, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(233, 233, 233))
        );

        pack();
    }

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void password_identifyActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void hand_inActionPerformed(java.awt.event.ActionEvent evt) {
        Account account = null;
        if (!username.getText().trim().equals("") && !password.getText().trim().equals("")) {
            account = new Account();
            account.setUsername(username.getText());
            account.setPassword(password.getText().hashCode());
            //TODO 在前台检验信息是否合法
            Boolean flag = false;
            flag = Client.getClient().getUserService().register(account);
            if (flag) {
                JOptionPane.showMessageDialog(this, "注册成功!");
            } else {
                JOptionPane.showMessageDialog(this, "注册失败, 请检查网络连接以及信息是否违法.");
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空!");
        }
        return;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //set flatlightlaf look and feel.
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //创建并绘制register的实例.
        java.awt.EventQueue.invokeLater(() -> {
            Register register = new Register();
            register.setLocationRelativeTo(null);
            register.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            register.setVisible(true);
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel app_intro;
    private javax.swing.JLabel author;
    private javax.swing.JButton hand_in;
    private javax.swing.JLabel identify_paswd;
    private javax.swing.JTextField password;
    private javax.swing.JTextField password_identify;
    private javax.swing.JLabel password_label;
    private javax.swing.JLabel register_font;
    private javax.swing.JSeparator separator_buttom;
    private javax.swing.JSeparator separator_top;
    private javax.swing.JTextField username;
    private javax.swing.JLabel username_label;
    // End of variables declaration
}

