package com.humility.client;

import com.humility.client.interfaces.ChatService;
import com.humility.client.interfaces.GoodService;
import com.humility.client.interfaces.TransactionService;
import com.humility.client.interfaces.UserService;
import com.humility.client.objectHandlers.KeepAliveHandler;
import com.humility.datas.KeepAlive;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * curriculum-design-client
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间2020年3月7日00:10:47
 */
@Slf4j
public class Client {

    //所有服务对象的代理.
    UserService userService;
    GoodService goodService;
    TransactionService transactionService;
    ChatService chatService;

    //自己持有自己的唯一实例
    private static Client client = null;

    public Socket getClientSocket() {
        return clientSocket;
    }

    //持有自己的心跳连接.
    private Socket clientSocket = null;

    //接受的对象和对应处理对象之间的映射.
    private ConcurrentHashMap<Class, com.humility.client.objectHandlers.ObjectHandler> actionMapping = new ConcurrentHashMap<>();

    //客户端状态. 包括登录的用户,运行状态,以及上次心跳的时间.
    private Integer me = null;
    private boolean running = false;
    private long lastSendTime;

    //服务器的公网ip地址.
    public static final String  SERVER_IP = "127.0.0.1";

    public Integer getMe() {
        return me;
    }

    public void setMe(Integer me) {
        this.me = me;
    }

    /**
     * 用于获取client唯一实例的公开接口.
     * @return
     */
    public static Client getClient() {
        //双重加锁.
        if (client == null) {
            synchronized(Client.class) {
                if (client == null) {
                    client = new Client();
                }
            }
        }
        return client;
    }

    public void addActionMap(Class<? extends Object> clas, com.humility.client.objectHandlers.ObjectHandler oh) {
        actionMapping.put(clas, oh);
    }

    /**
     * 通过长连接向服务端发送连接.
     * @param obj
     * @throws IOException
     */
    public void sendObject(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(obj);
        log.info("Send a object.");
        oos.flush();
    }

    /**
     * 开始正式运行客户端.
     * 在登录通过之后被调用,用来和服务器保持心跳连接.
     */
    public void start() {
        if (running) return;
        log.debug("Getting the longSocket...");
        try {
            Client.getClient().clientSocket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            String info = "Fail to get the socket long connection.";
            log.error(info);
            throw new RuntimeException(info, e);
        }
        log.debug("本地端口: " + clientSocket.getLocalPort());
        lastSendTime = System.currentTimeMillis();
        running = true;
        try {
            sendObject(me);
        } catch (IOException e) {
            String info = "Fail to send the Id! Please check the connection.";
            log.info(info);
            throw new RuntimeException(info, e);
            //TODO 网络连接异常处理逻辑.
        }
        new Thread(new KeepAliveWatchDog()).start();
        new Thread(new ReceiveWatchDog()).start();
    }

    /**
     * 客户端程序的入口
     * @param args  命令行参数
     */
    public static void main(String[] args) {
        log.debug("Initializing...");
        log.debug("Creating the client instance...");
        Client.getClient();
        log.debug("Initialization successful!");

        log.debug("Creating the user interface.");
        SwingUtilities.invokeLater(() -> client.startGUI());
        log.debug("GUI is running!");
    }

    /**
     * 简单的单例模式,私有的构造器防止外部创建client对象.
     */
    private Client() {
        log.debug("Initializing...");
        log.debug("Loading the userService.");
        userService = (UserService) Client.getProxy(UserService.class, SERVER_IP, USERSERVICE_PORT);
        log.debug("Success!");

        log.debug("Loading the goodService.");
        goodService = (GoodService) Client.getProxy(GoodService.class, SERVER_IP, GOODSERVICE_PORT);
        log.debug("Success!");

        log.debug("Loading the transactionService.");
        transactionService = (TransactionService) Client.getProxy(TransactionService.class, SERVER_IP, TRANSACTIONSERVICE_PORT);
        log.debug("Success!");

        log.debug("Loading the chatService.");
        chatService = (ChatService) Client.getProxy(ChatService.class, SERVER_IP, CHATSERVICE_PORT);
        log.debug("Success!");

        log.debug("Loading the objectHandlers...");
        this.addActionMap(KeepAlive.class, new KeepAliveHandler());
        log.debug("Initialize successful!");
    }

    /**
     * 创建远程代理.
     * @param clas  代理类型的接口.
     * @param ip    远程服务器的ip地址.
     * @param port  对应服务绑定的端口.
     * @return 返回代理对象本身.(Object类型,需要自己down cast.)
     */
    private static Object getProxy(Class clas, String ip, int port) {
        return Proxy.newProxyInstance(Client.class.getClassLoader(),
                new Class[] {clas},
                (Object proxy, Method method, Object[] args) -> {
                    Object ret = null;
                    Socket socket = null;
                    try {
                        socket = new Socket(ip, port);
                    } catch (IOException e) {
                        String info = "Invalid port num or ip address. Fail to get the socket connection.";
                        log.error(info);
                        //TODO 补上socket连接获取失败的处理逻辑.
                    }
                    String methodName = method.getName();
                    try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                        oos.writeUTF(methodName);
                        oos.writeObject(method.getParameterTypes());
                        oos.writeObject(args);
                        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                            ret = ois.readObject();
                        } catch (IOException e) {
                            String info = "Fail to transform the data!" + " While invoking the method: " + methodName;
                            log.error(info);
                            //TODO 补上方法调用失败(或者是输出流没问题,输入流出了问题)的处理逻辑.
                        }
                        } catch (IOException e) {
                        String info = "Fail to transform the data!" + " While invoking the method: " + methodName;
                        log.error(info);
                        //TODO io异常处理逻辑.
                    }
                    return ret;
                });
    }

    /**
     * 开始创建并运行gui界面.
     */
    void startGUI() {
        log.info("Login interface...");
        LoginGUI.getLoginGUI().createLoginGUI();
    }

    class KeepAliveWatchDog implements Runnable {

        long checkDelay = 100;
        long keepAliveDelay = 20000;

        @Override
        public void run() {
            while (running) {
                if (System.currentTimeMillis() - lastSendTime > keepAliveDelay) {
                    try {
                        Client.getClient().sendObject(new KeepAlive());
                        lastSendTime = System.currentTimeMillis();
                    } catch (IOException e) {
                        String info = "Fail to send the object, please check the internet connection.";
                        log.error(info);
                        Client.getClient().running = false;
                        //TODO 网络连接异常处理逻辑.
                    }
                } else {
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        log.error("InterruptedException!" + " In the KeepAliveWatchDog.");
                        //TODO 线程停止异常的处理逻辑.
                    }
                }
            }
        }
    }

    class ReceiveWatchDog implements Runnable {

        @Override
        public void run() {
            while (running) {
                try {
                    InputStream inputStream = Client.getClient().clientSocket.getInputStream();
                    if (inputStream.available() > 0) {
                        ObjectInputStream ois = new ObjectInputStream(inputStream);
                        Object obj = ois.readObject();
                        log.info("Get a object from server.");
                        com.humility.client.objectHandlers.ObjectHandler oh = actionMapping.get(obj.getClass());
                        oh.handleObejct(obj, Client.getClient());
                    }
                } catch (IOException e) {
                    log.error("There is something wrong with the internet connection." + " In the clientSocket.");
                    Client.getClient().running = false;
                    //TODO 网络连接异常的处理逻辑
                } catch (ClassNotFoundException e) {
                    log.error("Receive a unknown object." + " In the clientSocket.");
                    //TODO 接口错误的处理逻辑.
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("InterruptedException!!");
                }
            }
        }
    }


    //服务器各项服务的端口号
    public static final int USERSERVICE_PORT = 50001;
    public static final int GOODSERVICE_PORT = 50004;
    public static final int TRANSACTIONSERVICE_PORT = 50002;
    public static final int CHATSERVICE_PORT = 50003;
    public static final int SERVER_PORT = 50000;

}
