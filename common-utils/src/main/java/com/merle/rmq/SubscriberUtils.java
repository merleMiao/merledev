package com.merle.rmq;

import com.alibaba.druid.support.json.JSONUtils;
import com.merle.basic.Config;
import com.merle.io.JsonUtils;
import com.merle.util.uuid.UUIDUtils;
import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class SubscriberUtils {

    private final static Logger logger = LoggerFactory.getLogger(SubscriberUtils.class);

    private static String[] url = new String[]{"127.0.0.1"};

    private static int port = 5672;

    private static String user = "miao";

    private static String password = "admin";

    static {
        try {
            String[] _url = Config.getLocalProperties("rabbitmq.host");
            if (_url != null && _url.length > 0) {
                url = _url;
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    private final static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Address[] address = new Address[url.length];
        for (int i = 0; i < url.length; i++) {
            address[i] = new Address(url[i], port);
        }
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(10000);
        factory.setConnectionTimeout(10000);
        factory.setShutdownTimeout(10000);
        return factory.newConnection(address);
    }


    public static void addTopicListener(final String exchangeName, final String queueName, final AbsHandler handler) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = getConnection();
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(exchangeName, "fanout", true);
                    channel.queueDeclare(queueName, true, false, false, null);
                    channel.queueBind(queueName, exchangeName, "");
                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, true, consumer);
                    while (true) {
                        boolean result = false;
                        String msg = null;
                        try {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                            msg = new String(delivery.getBody(), "UTF-8");
                            result = handler.process(msg);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            logger.error(result + "\t" + msg + "\t" + ex.getMessage());
                        } finally {
                            logger.info(result + "\t" + msg);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
//                    try {
//                        connection.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Thread.sleep(5000l);
//                        addTopicListener(exchangeName, queueName, handler);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } finally {


                }
            }
        };
        new Thread(runnable).start();
    }


    public static void addTopicListener(final String exchangeName, final String queueName, final boolean autoAck, final AbsHandler handler) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = getConnection();
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(exchangeName, "fanout", true);
                    channel.queueDeclare(queueName, true, false, false, null);
                    channel.queueBind(queueName, exchangeName, "");
                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(queueName, autoAck, consumer);
                    while (true) {
                        boolean result = false;
                        String msg = null;
                        QueueingConsumer.Delivery delivery = null;
                        try {
                            delivery = consumer.nextDelivery();
                            msg = new String(delivery.getBody(), "UTF-8");
                            result = handler.process(msg);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.error(result + "\t" + msg + "\t" + ex.getMessage());
                        } finally {
                            logger.info(delivery.getEnvelope().getDeliveryTag() + "===" + result + "===" + msg);
                            if (result) {
                                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                            } else {
                                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    public static void sendMessage(final Object object, final String exchangeName) {
        logger.info("rmq_send_message:start\t" + JsonUtils.toJson(object));
        class Task implements Callable<Boolean> {
            @Override
            public Boolean call() throws Exception {
                Connection connection = null;
                Channel channel = null;
                boolean result = false;
                try {
                    connection = getConnection();
                    channel = connection.createChannel();
                    channel.exchangeDeclare(exchangeName, "fanout", true);
                    String message = StringUtils.defaultString(JsonUtils.toJson(object));
                    channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
                    result = true;
                } catch (Exception ex) {
                    result = false;
                    logger.info("rmq_send_message:\t" + result + "\t" + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    logger.info("rmq_send_message:\t" + result + "\t" + JsonUtils.toJson(object));
                    closeChannel(channel);
                    closeConnection(connection);
                }
                return result;
            }
        }
        final Task task = new Task();
        final ExecutorService exec = Executors.newFixedThreadPool(1);
        Boolean result = false;
        try {
            Future<Boolean> future = exec.submit(task);
            result = future.get(5000l, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("rmq_send_message:all\t" + result + "\t" + JsonUtils.toJson(object));
            exec.shutdown();
        }
    }

    private final static void closeChannel(Channel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static String getHostName() {
        String name = "";
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            name = netAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return StringUtils.defaultString(name);
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SubscriberUtils.addTopicListener("xxx", "xxx_handler", false, new AbsHandler() {

                    @Override
                    public boolean process(String message) {
                        boolean b = RandomUtils.nextBoolean();

                        return b;
                    }
                });
            }
        });
        thread.start();
        while (true) {
            try {
                Map map = new HashMap();
                map.put("uuid", UUIDUtils.base58Uuid());
                sendMessage(map, "xxx");
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
