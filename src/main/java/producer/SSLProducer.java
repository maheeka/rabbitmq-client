package producer;

import com.rabbitmq.client.*;

/**
 * Created by maheeka on 6/26/15.
 */
public class SSLProducer {



        public static void main(String[] args) throws Exception
        {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5671);

            factory.useSslProtocol();
            // Tells the library to setup the default Key and Trust managers for you
            // which do not do any form of remote server trust verification

            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();

            //non-durable, exclusive, auto-delete queue
            channel.queueDeclare("queuessl", false, false, false, null);

            String message = "<m:placeOrder xmlns:m=\"http://services.samples\">\n"
                    + "    <m:order>\n" + "        <m:price>" + 100
                    + "</m:price>\n" + "        <m:quantity>" + 100
                    + "</m:quantity>\n" + "        <m:symbol>" + "XX"
                    + "</m:symbol>\n" + "    </m:order>\n" + "</m:placeOrder>";
            channel.basicPublish("", "queuessl", null, message.getBytes());

            GetResponse chResponse = channel.basicGet("queuessl", false);
            if(chResponse == null) {
                System.out.println("No message retrieved");
            } else {
                byte[] body = chResponse.getBody();
                System.out.println("Recieved: " + new String(body));
            }


            channel.close();
            conn.close();
        }

}
