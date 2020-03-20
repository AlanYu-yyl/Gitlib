package com.humility.server;

import com.humility.datas.KeepAlive;
import com.humility.server.objectHandler.KeepAliveHandler;
import com.humility.server.objectHandler.ObjectHandler;
import com.humility.utils.JDBCUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * curriculum-design-server
 * @author Humility <Yiling Yu>
 * @version 1.0.0
 * 创建时间 2020年3月6日23:44:31
 */
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
    public static void main(String[] args) {
        Server.getServer().start();
    }

    /**
     * 向服务器注册服务.
     * @param service   服务对象.
     * @param port      绑定的端口号.
     */
    public void register(@org.jetbrains.annotations.NotNull Object service, int port) {
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

    //默认线程数量.
    public static final int DEFAULT_THREAD_MIN_NUM = 5;
    public static final int DEFAULT_THREAD_MAX_NUM = 15;

    //端口号
    public static final int PORT = 50010;

     //简单的单例模式,类持有自己惟一的静态实例.
    private static Server server;

    //数据库连接池.
    private DataSource dataSource = null;

    //线程池.
    private ExecutorService threadPool = null;

    //封装数据库处理.
    static JDBCUtils jdbcUtils = new JDBCUtils();

    //客户端在线列表.(使用tcp长连接维护)通过userId映射到对应的socket长连接.
    private Map<Socket, Long> clientStatus = new HashMap<>();

    //用户id映射,把用户id映射成对应的socket连接
    private Map<Integer, Socket> idMapper = new HashMap<>();

    //将接收到的对象映射到处理对象上.
    private ConcurrentHashMap<Class, ObjectHandler> actionMapping = new ConcurrentHashMap<>();

    //维持长连接的端口.
    private ServerSocket serverSocket = null;

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
     * 添加用户的在线状态 -- 用id到上次连接的时间戳的映射来表示
     * @param socket
     * @param timeMillis
     */
    private void addOnlineUserStatus(Socket socket, Long timeMillis) {
        clientStatus.put(socket, timeMillis);
        log.info("A new user online.");
    }

    /**
     * 添加长连接接受对象的处理方法 -- 通过接受到的对象的class映射到对应的handler.
     * @param clas
     * @param handler
     */
    private void addActionMap(Class<? extends Object> clas, ObjectHandler handler) {
        actionMapping.put(clas, handler);
    }

    /**
     * 设置在线用户的状态, 在接收到心跳的时候更新该用户的在线状态.
     * @param socket
     * @param timeMillis
     */
    private void setOnlineUserStatus(Socket socket, Long timeMillis) {
        long lastReceiveTime = clientStatus.get(socket);
        if (clientStatus.get(socket) != null) {
            clientStatus.replace(socket, lastReceiveTime, timeMillis);
        } else {
            addOnlineUserStatus(socket, timeMillis);
        }
    }

    /**
     * 服务器的启动方法
     */
    private void start() {
        server.addObjectHandler();
        UserService userService = new UserServiceImpl();
        threadPool.submit(() -> server.register(userService, UserServiceImpl.PORT));
        ChatService chatService = new ChatServiceImpl();
        threadPool.submit(() -> server.register(chatService,ChatServiceImpl.PORT));
        GoodService goodService = new GoodServiceImpl();
        threadPool.submit(() -> server.register(goodService,GoodServiceImpl.PORT));
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
            this.addOnlineUserStatus(socket, System.currentTimeMillis());
            Thread socketChecker = new Thread(new SocketChecker(socket));
            socketChecker.setPriority(2);
            socketChecker.start();
        }
    }

    /**
     * 添加用户到对应socket长连接的映射 -- 用于服务端向客户端推送消息
     * @param id
     * @param socket
     */
    private void addIdMapper(Integer id, Socket socket) {
        idMapper.put(id, socket);
        log.info("get a IdMapper!!!");
    }

    /**
     * @author Humility <Yiling Yu>
     * @version 1.0.0
     * 创建时间 2020年3月10日22:41:10
     */
    private class SocketChecker implements Runnable {
        Socket socket = null;

        public SocketChecker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (System.currentTimeMillis() - clientStatus.get(socket) < 40000) {
                try {
                    InputStream in = socket.getInputStream();
                    if (in.available() > 0) {
                        ObjectInputStream ois = new ObjectInputStream(in);
                        Object obj = ois.readObject();
                        setOnlineUserStatus(socket, System.currentTimeMillis());
                        Class objClass = obj.getClass();
                        Object out = null;
                        if (objClass.equals(Integer.class)) {
                            addIdMapper((Integer)obj, socket);
                        } else {
                            ObjectHandler oh = actionMapping.get(objClass);
                            out = oh.handleObject(obj);
                        }
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
                    closeTheSocket(socket);
                } catch (ClassNotFoundException e) {
                    String info = "Get a unknown object." + "Oops!!!!";
                    log.error(info);
                    throw new RuntimeException(info, e);
                }
            }
            log.info("The socket: " + socket.getRemoteSocketAddress() + "  is disconnected.");
            closeTheSocket(socket);
        }
    }

    /**
     * 关闭tcp长连接的辅助方法
     */
    private void closeTheSocket(Socket socket) {
        if (socket != null) {
            clientStatus.remove(socket);

            try{
                socket.close();
            } catch (IOException e) {
                log.error("Fail to close the socket.");
            }
        }
    }

    /**
     * 添加长连接接收到的对象的处理方法
     */
    private void addObjectHandler() {
        log.debug("Adding objectHandler...");
        addActionMap(KeepAlive.class, new KeepAliveHandler());
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
}

