package rpc;

import com.rabbitmq.client.*;

/**
 * Created by maheeka on 6/29/15.
 */
public class RPCClient {
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "queue1";
    private String replyQueueName="reply_queue";
    private QueueingConsumer consumer;

    public RPCClient() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

//        replyQueueName = channel.queueDeclare().getQueue();
        channel.queueDeclare(replyQueueName, false, false, false, null);
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = java.util.UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody());
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        RPCClient fibonacciRpc = new RPCClient();

        System.out.println(" [x] Requesting fib(30)");
        String response = fibonacciRpc.call("30");
        System.out.println(" [.] Got '" + response + "'");

        fibonacciRpc.close();

    }

}
