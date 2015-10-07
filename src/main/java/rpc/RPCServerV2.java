package rpc;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCServerV2 {

    //private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] argv) {
        Connection connection = null;
        Channel channel;
        int counter = 1;
        long startTime = 0;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            //channel.queueDeclare(, true, false, false, null);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume("queue1", false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (true) {
                String response = null;
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                counter ++;
                //System.out.println(counter);
                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties.Builder().correlationId(props.getCorrelationId()).contentType("application/json")
                        .build();
                response = "{\"response\":\"yes this is response\"}";

                //System.out.println("Received : ".concat(new String(delivery.getBody(), "UTF-8")));
                String replyToQueue = props.getReplyTo();
                //System.out.println(counter + " Publishing to : " + replyToQueue);
//                System.out.println("Reply to : " + replyToQueue);
//                System.out.println("Publishing : " + response);
                channel.basicPublish("", replyToQueue, replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}


