package consumer;

import client.RabbitMQConsumerClient;

import java.io.IOException;
import java.util.List;

public class BasicRabbitMQQueueConsumer {

    private final static String ROUTE_KEY = "BasicQueue";
    private final static String EXCHANGE_NAME = "BasicExchange";
    private final static String HOST = "localhost";

    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitMQConsumerClient consumerClient = new RabbitMQConsumerClient(HOST);
        consumerClient.connect(EXCHANGE_NAME, ROUTE_KEY);

        List<String> messages = consumerClient.popAllMessages();

        if (messages == null || messages.size() == 0) {
            System.err.println("Messages not received at RabbitMQ Broker");
        } else {
            for (int i = 0; i < messages.size(); i++) {
                System.out.println("Received message " + i + " = " + messages.get(i));
            }
        }

        System.out.println("Received message count = " + messages.size());
        consumerClient.disconnect();
    }
}