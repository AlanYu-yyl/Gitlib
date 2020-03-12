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
public class LoginInterface implements ActionListener {

    //持有需要监听器处理的对象
    private JTextField username = null;
    private JPasswordField password = null;
    private JButton login = null;
    private JButton register = null;
    private JFrame jf = null;

    //持有自己的私有静态对象,用于监听按钮.
    private static LoginInterface loginInterface = new LoginInterface();

    /**
     * 获取自己唯一实例的公开接口.
     */
    public static LoginInterface getLoginInterface() {
        return loginInterface;
    }
    /**
     * 创建登录和注册的gui界面.
     */
    void createLoginGUI() {
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
        login.addActionListener(loginInterface);
        register.addActionListener(loginInterface);

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
     * 监听登录和注册两个button.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == login) {
            User me = Client.getClient().userService.login(new Account(username.getText(), new String(password.getPassword()).hashCode()));
            username.setText("");
            password.setText("");
            if (me == null) {
                log.info("登录失败");
                JOptionPane.showMessageDialog(jf, "用户名或密码错误,请重新输入.");
            } else {
                Client.getClient().setMe(me);
                Client.getClient().start();
                JOptionPane.showMessageDialog(jf, "登录成功");
            }
        }
        if (e.getSource() == register) {
            RegisterInterface.getRegisterInterface().createRegisterGUI();
        }
    }

    /**
     * 简单的测试方法.
     * @param args
     */
    public static void main(String[] args) {
        LoginInterface.loginInterface.createLoginGUI();
    }

    private LoginInterface() {
    }

}
