package producer;

import client.RabbitMQProducerClient;
import util.RabbitMQClientUtils;

import java.io.IOException;

public class BasicRabbitMQQueueProducer {
    private final static String ROUTE_KEY = "BasicQueue";
    private final static String EXCHANGE_NAME = "BasicExchange";
    private final static String HOST = "localhost";
    private final static int PORT = 5672;
    private final static String USERNAME = "guest";
    private final static String PASSWORD = "guest";
    private final static int MESSAGE_COUNT = 111;

    public static void main(String[] args) throws IOException {
        RabbitMQProducerClient producer = new RabbitMQProducerClient(HOST, PORT, USERNAME, PASSWORD);
        producer.connect(EXCHANGE_NAME, ROUTE_KEY);

        String payload = RabbitMQClientUtils.DEFAULT_PLACEORDER_PAYLOAD;

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            producer.sendBasicMessage(payload);
        }

        producer.disconnect();
    }

}
