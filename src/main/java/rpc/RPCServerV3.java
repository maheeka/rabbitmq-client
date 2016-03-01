package rpc;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCServerV3 {

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
           // channel.queueDeclare("queue3", true, false, false, null);
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume("queue3", false, consumer);

            System.out.println(" [x] Awaiting RPC requests");

            while (true) {
                String response = null;
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                counter ++;
                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties.Builder().correlationId(props.getCorrelationId()).contentType("text/xml")
                        .build();

               response = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services.samples\" xmlns:xsd=\"http://services.samples/xsd\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <ser:placeOrder>\n" +
                        "         <!--Optional:-->\n" +
                        "         <ser:order>\n" +
                        "            <!--Optional:-->\n" +
                        "            <xsd:price>10</xsd:price>\n" +
                        "            <!--Optional:-->\n" +
                        "            <xsd:quantity>5</xsd:quantity>\n" +
                        "            <!--Optional:-->\n" +
                        "            <xsd:symbol>RMQ</xsd:symbol>\n" +
                        "         </ser:order>\n" +
                        "      </ser:placeOrder>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

                String replyToQueue = props.getReplyTo();
                System.out.println(counter + " Publishing to : " + replyToQueue);
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


