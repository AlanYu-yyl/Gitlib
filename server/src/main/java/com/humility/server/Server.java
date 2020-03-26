package com.humility.server;

/**
 * curriculum-design-server
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月6日23:44:31
 */

import com.humility.datas.KeepAlive;
import com.humility.server.objectHandler.KeepAliveHandler;
import com.humility.server.objectHandler.ObjectHandler;
import com.humility.utils.JDBCUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class Server {

    /**
     * 单例模式,用于获取服务器唯一实例.
     * @return
     */
    public static Server getServer() {
        if (server == null) {
            synchronized(Server.class) {
                if (server == null) {
                    server = new Server();
                }
                return server;
            }
        }
        else {
            return server;
        }
    }

    /**
     * 获取数据库连接池.
     * @return
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 服务器的启动方法.
     * @param args  命令行参数.
     */
    public static void main(String args[]) {
        Server.getServer().start();
    }

    /**
     * 在用户列表添加新的用户
     * @param id 用户的注册id.
     */
    public void addRegisteredUser(Integer id) {
        ClientStatus clientStatus = new ClientStatus();
        clientStatus.setStatus(ClientStatus.Status.OFFLINE);
        this.clientStatusMap.put(id, clientStatus);
        log.info("A new user registered.");
    }

    /**
     * 更改用户状态, 将登陆的用户状态改为Online, 将心跳消失的用户状态改为Offline
     * @param user_id    用户id
     * @param status     用户需要设置的状态
     */
    public void setUserStatus(Integer user_id, ClientStatus.Status status) {
        clientStatusMap.get(user_id).setStatus(status);
        String statusStr = "OffLine";
        if (status == ClientStatus.Status.ONLINE) {
            statusStr = "OnLine";
        }
        log.info(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now()) + ": User " + user_id + " is " + statusStr + ".");
    }



    //默认线程数量.
    public static final int DEFAULT_THREAD_MIN_NUM = 5;
    public static final int DEFAULT_THREAD_MAX_NUM = 15;

    //端口号
    public static final int PORT = 50000;

    //封装数据库处理.
    static JDBCUtils jdbcUtils = new JDBCUtils();



    /**
     * 私有构造器,防止外部创建server实例.
     */
    private Server() {
        log.debug("Initializing...");
        log.debug("Creating the server...");
        log.debug("Bending the Service...");
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            String info = "The port: " + PORT + " might have been bended.";
            log.error(info);
            throw new RuntimeException(info, e);
        }
        this.threadPool = new ThreadPoolExecutor(
                DEFAULT_THREAD_MIN_NUM,
                DEFAULT_THREAD_MAX_NUM,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        log.debug("Success to get the threadPool!");
        log.debug("Trying to get the dbcp...");
        HikariConfig config = new HikariConfig();
        //TODO 记得更换相应路径.
        String fileName = "D:\\Programme\\workspace\\java\\idea\\curriculum-design\\server\\src\\main\\resources\\dbcp.properties";
        Properties dbcpConfig = null;
        try (FileReader fr = new FileReader(fileName)) {
            dbcpConfig = new Properties();
            dbcpConfig.load(fr);
        } catch (FileNotFoundException fnfe) {
            String info = "Cannot find the configFile: " + fileName;
            log.error(info);
            throw new RuntimeException(info, fnfe);
        } catch (IOException ioe) {
            String info = "Fail to load the dbcp config.";
            log.error(info);
            throw new RuntimeException(info ,ioe);
        }
        getDBCPConfig(config, dbcpConfig);
        dataSource = new HikariDataSource(config);
        log.debug("Success to get the dataSource");
    }

    /**
     * 服务器的启动方法
     */
    private void start() {
        log.info("Loading the info of all users...");
        server.loadUsersInfo();
        log.info("Success!");
        server.addObjectHandler();
        UserService userService = new UserServiceImpl();
        threadPool.submit(() -> server.register(userService, UserServiceImpl.PORT));
        ChatService chatService = new ChatServiceImpl();
        threadPool.submit(() -> server.register(chatService, ChatServiceImpl.PORT));
        GoodService goodService = new GoodServiceImpl();
        threadPool.submit(() -> server.register(goodService, GoodServiceImpl.PORT));
        TransactionService transactionService = new TransactionServiceImpl();
        threadPool.submit(() -> server.register(transactionService, TransactionServiceImpl.PORT));
        //开一个线程检查客户端的活跃性.
        threadPool.submit(() -> Server.getServer().handleLongCon());
    }

    /**
     * 长连接处理方法, 遍历用户状态 找出长时间没有心跳的客户端,把它删去.
     */
    private void handleLongCon() {
        while (true) {
            Socket socket = null;
            try {
                socket = Server.getServer().serverSocket.accept();
            } catch (IOException e) {
                log.error("Fail to get the socket connection." + " In the server.");
                //TODO 处理逻辑.
            }
            Thread socketChecker = new Thread(new SocketChecker(socket));
            socketChecker.setPriority(2);
            socketChecker.start();
        }
    }

    /**
     * 向服务器注册服务.
     * @param service   服务对象.
     * @param port      绑定的端口号.
     */
    private void register(@org.jetbrains.annotations.NotNull Object service, int port) {
        String serverName = service.getClass().getName();
        log.debug("Registering the " + serverName);
        try {
            ServerSocket server = new ServerSocket(port);
            log.debug(serverName + " starts.");
            Socket socket = null;
            try {
                while ((socket = server.accept()) != null) {
                    log.info("In the " + serverName + ": get a client connected...");
                    threadPool.execute(new Task(service, socket));
                }
            } catch (IOException e) {
                log.error("Fail to build the connection in port " + port);
            }
        } catch (IOException e) {
            String info = "Fail to get the serviceSocket!" + "  Maybe the port has been occupied.";
            log.error(info);
            throw new RuntimeException(info, e);
        }
    }

    private void loadUsersInfo() {
        List<Object[]> usersInfo = jdbcUtils.queryAllUsers();
        for (Iterator<Object[]> iter = usersInfo.iterator(); iter.hasNext();) {
            ClientStatus clientStatus = new ClientStatus();
            clientStatus.setStatus(ClientStatus.Status.OFFLINE);
            clientStatusMap.put((Integer)iter.next()[0], clientStatus);
        }
    }

    /**
     * 添加长连接接收到的对象的处理方法
     */
    private void addObjectHandler() {
        log.debug("Adding objectHandler...");
        actionMapping.put(KeepAlive.class, new KeepAliveHandler());
        log.debug("Success!");
    }

    /**
     * 封装数据库连接池属性的读取.
     * 将具体配置读取到配置对象之中.
     * @param config        配置对象
     * @param dbcpConfig    具体配置
     */
    private void getDBCPConfig(@NotNull HikariConfig config, @NotNull Properties dbcpConfig) {
        config.setDriverClassName(dbcpConfig.getProperty("driver"));
        config.setJdbcUrl(dbcpConfig.getProperty("url"));
        config.setUsername(dbcpConfig.getProperty("username"));
        config.setPassword(dbcpConfig.getProperty("password"));
        config.addDataSourceProperty("connectionTimeout", dbcpConfig.getProperty("connectionTimeout"));
        config.addDataSourceProperty("idleTimeout", dbcpConfig.getProperty("idleTimeout"));
        config.addDataSourceProperty("maximumPoolSize", dbcpConfig.getProperty("maximumPoolSize"));
    }

    //简单的单例模式,类持有自己惟一的静态实例.
    private static Server server;

    //维持长连接的端口.
    private ServerSocket serverSocket = null;

    //数据库连接池.
    private DataSource dataSource = null;

    //线程池.
    private ExecutorService threadPool = null;

    //客户端状态映射
    private Map<Integer, ClientStatus> clientStatusMap = new HashMap<>();

    //将接收到的对象映射到处理对象上.
    private ConcurrentHashMap<Class, ObjectHandler> actionMapping = new ConcurrentHashMap<>();



    /**
     * 表示用户状态的Data类
     */
    @Data
    @NoArgsConstructor
    static class ClientStatus {
        /**
         * 用于表示用户在线状态的枚举
         */
        enum Status {
            OFFLINE, ONLINE;
        }

        Status status;
        long lastReceiveTime;
        Socket socket;
    }

    /**
     * Server的内部类.用于处理客户端通过远程代理调用的服务.
     * @author Humility <Yiling Yu>
     * @version 1.0.0
     * 创建时间 2020年3月6日23:51:57
     */
    private class Task implements Runnable {
        Object service;
        Socket socket;

        public Task(Object service, Socket socket) {
            this.service = service;
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                String methodName = ois.readUTF();
                log.info("Getting a new request.  MethodName: " + methodName);
                Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
                Object[] parameters = (Object[]) ois.readObject();
                Method method = service.getClass().getMethod(methodName, parameterTypes);
                try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                    Object result = method.invoke(service, parameters);
                    oos.writeObject(result);
                    oos.flush();
                } catch (IllegalAccessException e) {
                    String info = "Illegal Access to the Method: " + methodName;
                    log.error(info);
                } catch (IllegalArgumentException e) {
                    String info = "Illegal Argument to the Method: " + methodName;
                    log.error(info);
                    throw new RuntimeException(info, e);
                } catch (InvocationTargetException e) {
                    String info = "Wrong Invocation Target in the Method: " + methodName;
                    log.error(info);
                    throw new RuntimeException(info, e);
                }
            } catch (IOException e) {
                log.error("Fail to transform the data!");
            } catch (NoSuchMethodException e) {
                log.warn("Client invoke the nonexistent method!");
            } catch (SecurityException e) {
                log.warn("Illegal Access!");
            } catch (ClassNotFoundException e) {
                String info = "Cannot find the service class";
                log.error(info);
                throw new RuntimeException(info, e);
            }
        }
    }

    /**
     * 用于处理客户端tcp长连接的线程类
     * @author Humility <Yiling Yu>
     * @version 1.0.0
     * 创建时间 2020年3月10日22:41:10
     */
    private class SocketChecker implements Runnable {
        Socket socket;
        Integer userId = null;

        public SocketChecker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            do {
                try {
                    InputStream in = socket.getInputStream();
                    if (in.available() > 0) {
                        ObjectInputStream ois = new ObjectInputStream(in);
                        Object obj = ois.readObject();
                        Class objClass = obj.getClass();
                        Object out = null;
                        if (objClass.equals(Integer.class)) {
                            userId = (Integer)obj;
                            clientStatusMap.get(userId).setSocket(socket);
                        } else {
                            ObjectHandler oh = actionMapping.get(objClass);
                            out = oh.handleObject(obj);
                        }
                        clientStatusMap.get(userId).setLastReceiveTime(System.currentTimeMillis());
                        if (out != null) {
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(out);
                            log.info(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now()) + ":  Handled the obj and send a response.");
                        }
                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            log.error("Fail to stop the thread.");
                        }
                    }
                } catch (IOException e) {
                    log.info("The socket is disconnected.");
                    disconnect(userId);
                } catch (ClassNotFoundException e) {
                    String info = "Get a unknown object." + "Oops!!!!";
                    log.error(info);
                    throw new RuntimeException(info, e);
                }
            } while (System.currentTimeMillis() - clientStatusMap.get(userId).getLastReceiveTime() < 40000);
            log.info("The socket: " + socket.getRemoteSocketAddress() + "  is disconnected.");
            disconnect(userId);
        }

        /**
         * 在用户心跳断开的时候调用的断开连接方法
         * 关闭socket连接并通过用户id将其status设置成OFFLINE;
         * @param userId
         */
        private void disconnect(Integer userId) {
            Socket socket = clientStatusMap.get(userId).getSocket();
            clientStatusMap.get(userId).setStatus(ClientStatus.Status.OFFLINE);
            if (socket != null) {
                try{
                    socket.close();
                } catch (IOException e) {
                    log.error("Fail to close the socket.");
                }
            }
        }
    }

}