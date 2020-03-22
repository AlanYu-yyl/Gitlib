package com.humility.client;

import com.humility.datas.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI implements ActionListener {

    //ui组件
    private JFrame     jf           = null;
    private JButton    handIn       = null;
    private JTextField username     = null;
    private JTextField password     = null;
    private JTextField realname     = null;
    private JTextField phoneNumber  = null;
    private JTextField emailAddress = null;
    private JTextField qqAccount    = null;

    //持有自己的唯一实例.
    private static RegisterGUI registerGUI = new RegisterGUI();

    /**
     * 获取自己静态实例的唯一公开接口.
     * @return
     */
    public static RegisterGUI getRegisterGUI() {
        return registerGUI;
    }

    private RegisterGUI() {
    }

    /**
     * 创建注册的ui界面.
     */
    void createRegisterGUI() {
        jf = new JFrame("注册界面");

        JLabel inputUsername      = new JLabel("用  户  名");
        JLabel inputPassword      = new JLabel("密      码");
        JLabel inputRealname      = new JLabel("真实姓名");
        JLabel inputPhoneNumber   = new JLabel("手机号码");
        JLabel inputEmailAddress  = new JLabel("电子邮箱");
        JLabel inputQqAccount     = new JLabel("QQ账号");

        username       = new JTextField(10);
        password       = new JTextField(10);
        realname       = new JTextField(10);
        phoneNumber    = new JTextField(10);
        emailAddress   = new JTextField(10);
        qqAccount      = new JTextField(10);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();

        panel1.add(inputUsername);
        panel1.add(username);
        panel2.add(inputPassword);
        panel2.add(password);
        panel3.add(inputRealname);
        panel3.add(realname);
        panel4.add(inputPhoneNumber);
        panel4.add(phoneNumber);
        panel5.add(inputEmailAddress);
        panel5.add(emailAddress);
        panel6.add(inputQqAccount);
        panel6.add(qqAccount);

        Box vbox = Box.createVerticalBox();
        vbox.add(panel1);
        vbox.add(panel2);
        vbox.add(panel3);
        vbox.add(panel4);
        vbox.add(panel5);
        vbox.add(panel6);

        handIn = new JButton("提交");
        handIn.addActionListener(registerGUI);

        vbox.add(handIn);

        jf.setContentPane(vbox);
        jf.pack();
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == handIn) {
            User user = new User();
            user.setUser_id(0);
            if (!username.getText().trim().equals("") && !password.getText().trim().equals("")) {
                user.setUsername(username.getText());
                user.setPassword(password.getText().hashCode());
                user.setRealname(realname.getText());
                user.setPhoneNumber(phoneNumber.getText());
                user.setEmailAddress(emailAddress.getText());
                user.setQqAccount(qqAccount.getText());
                //TODO 在前台检验信息是否合法
                Boolean flag = false;
                flag = Client.getClient().userService.register(user);
                if (flag) {
                    JOptionPane.showMessageDialog(jf, "注册成功!");
                    jf.dispose();
                } else {
                    JOptionPane.showMessageDialog(jf, "注册失败, 请检查信息是否违法以及网络连接.");
                }

            } else {
                JOptionPane.showMessageDialog(jf, "用户名和密码不能为空!");
            }
        }
    }
}
