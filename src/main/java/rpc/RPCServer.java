package rpc;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by maheeka on 6/29/15.
 */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "queueio";//"rpc_queue";
    private static final String REPLY_TO_QUEUE = "reply_queue";
    public static void main(String[] args) throws Exception {


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setAutomaticRecoveryEnabled(true);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(REPLY_TO_QUEUE, false, false, false, null);

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(RPC_QUEUE_NAME, true, consumer);

        System.out.println(" [x] Awaiting RPC requests");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("Received ..");
            BasicProperties props = delivery.getProperties();
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(props.getCorrelationId())
                    .contentType("text/xml")
                    .build();

            String message = new String(delivery.getBody());
            //int n = Integer.parseInt(message);

            System.out.println(message);
//            String response = "" + fib(n);
//            String response = "<ser:placeOrder xmlns:ser=\"http://services.samples\">"
//                    + "<ser:order>"
//                    + "<ser:price>100</ser:price>"
//                    + "<ser:quantity>2000</ser:quantity>"
//                    + "<ser:symbol>RMQ</ser:symbol>"
//                    + "</ser:order>"
//                    + "</ser:placeOrder>";
//            System.out.println(response);

            String response = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services.samples\" xmlns:xsd=\"http://services.samples/xsd\">\n" +
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

            String response2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <ns:getQuoteResponse xmlns:ns=\"http://services.samples\">\n" +
                    "         <ns:return xsi:type=\"ax21:GetQuoteResponse\" xmlns:ax21=\"http://services.samples/xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "            <ax21:change>-2.307529516560527</ax21:change>\n" +
                    "            <ax21:earnings>13.711396909220584</ax21:earnings>\n" +
                    "            <ax21:high>93.22947202265465</ax21:high>\n" +
                    "            <ax21:last>89.68654810308918</ax21:last>\n" +
                    "            <ax21:lastTradeTimestamp>Thu Jul 02 14:43:14 IST 2015</ax21:lastTradeTimestamp>\n" +
                    "            <ax21:low>91.96837956316695</ax21:low>\n" +
                    "            <ax21:marketCap>1378663.856702298</ax21:marketCap>\n" +
                    "            <ax21:name>TEst Company</ax21:name>\n" +
                    "            <ax21:open>92.41547602633148</ax21:open>\n" +
                    "            <ax21:peRatio>24.956326161022936</ax21:peRatio>\n" +
                    "            <ax21:percentageChange>2.6418088200240923</ax21:percentageChange>\n" +
                    "            <ax21:prevClose>-87.34657478126987</ax21:prevClose>\n" +
                    "            <ax21:symbol>TEst</ax21:symbol>\n" +
                    "            <ax21:volume>19780</ax21:volume>\n" +
                    "         </ns:return>\n" +
                    "      </ns:getQuoteResponse>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            System.out.println(props.getCorrelationId());
            channel = connection.createChannel();
            //channel.basicPublish( "", props.getReplyTo(), replyProps, response.getBytes());
            channel.basicPublish( "", REPLY_TO_QUEUE, replyProps, response2.getBytes());

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

    }


}
