package com.humility.client;

import com.humility.datas.Account;
import com.humility.datas.User;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 登录界面.
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月7日00:15:00
 */
@Slf4j
public class LoginGUI implements ActionListener {

    /**
     * 获取自己的静态实例
     */
    public static LoginGUI getLoginGUI() {
        if (loginGUI == null) {
            synchronized (LoginGUI.class) {
                if (loginGUI == null) {
                    loginGUI = new LoginGUI();
                }
            }
        }
        return loginGUI;
    }

    /**
     * 创建登录和注册的gui界面.
     */
    public void createLoginGUI() {
        jf = new JFrame("用户登录");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        JLabel inputUsername = new JLabel("用户名");
        JLabel inputPassword = new JLabel("密   码");

        username = new JTextField(10);
        password = new JPasswordField(10);
        login = new JButton("登录");
        register = new JButton("注册");
        login.addActionListener(loginGUI);
        register.addActionListener(loginGUI);

        panel1.add(inputUsername);
        panel2.add(inputPassword);
        panel1.add(username);
        panel2.add(password);
        panel3.add(login);
        panel3.add(register);

        Box vBox = Box.createVerticalBox();
        vBox.add(panel1);
        vBox.add(panel2);
        vBox.add(panel3);

        jf.setContentPane(vBox);
        jf.setLocationRelativeTo(null);
        jf.pack();
        jf.setVisible(true);
    }

    /**
     * 实现登录界面用户交互逻辑.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //点击登录触发的行为.
        if(e.getSource() == login) {
            User user = Client.getClient().userService.login(new Account(username.getText(), new String(password.getPassword()).hashCode()));
            username.setText("");
            password.setText("");
            if (user == null) {
                log.info("登录失败");
                JOptionPane.showMessageDialog(jf, "用户名或密码错误,请重新输入.");
            } else {
                Client.getClient().me = user.getUser_id();
                Client.getClient().start();
                JOptionPane.showMessageDialog(jf, "登录成功");
            }
            return;
        }

        //点击注册触发的行为.
        if (e.getSource() == register) {
            RegisterGUI.getRegisterGUI().createRegisterGUI();
            return;
        }

    }

    /**
     * 一个注册界面的GUI.
     * @author Humility <Yiling_Yu>
     * @version 1.0.0
     * 创建时间 2020年3月26日20:59:44
     */
    private static class RegisterGUI implements ActionListener{

        /**
         * 获取自己的唯一实例.
         * @return
         */
        public static RegisterGUI getRegisterGUI() {
            if (registerGUI == null) {
                synchronized (RegisterGUI.class) {
                    if (registerGUI == null) {
                        registerGUI = new RegisterGUI();
                    }
                }
            }
            return registerGUI;
        }

        /**
         * 注册界面的监听器.
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            //注册页面的提交键.
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
                return;
            }
        }

        /**
         * 创建注册的ui界面.
         */
        private void createRegisterGUI() {
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

        //持有自己的静态实例.
        private static RegisterGUI registerGUI = null;

        //ui组件
        private JFrame     jf           = null;
        private JButton    handIn       = null;
        private JTextField username     = null;
        private JTextField password     = null;
        private JTextField realname     = null;
        private JTextField phoneNumber  = null;
        private JTextField emailAddress = null;
        private JTextField qqAccount    = null;

    }


    //持有自己的静态私有实例.
    private static LoginGUI loginGUI = null;

    //持有需要监听器处理的对象
    private JTextField username = null;
    private JPasswordField password = null;
    private JButton login = null;
    private JButton register = null;
    private JFrame jf = null;

}





