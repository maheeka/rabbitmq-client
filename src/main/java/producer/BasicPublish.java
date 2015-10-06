package producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by maheeka on 6/26/15.
 */
public class BasicPublish {


    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        //factory.useSslProtocol();
        // Tells the library to setup the default Key and Trust managers for you
        // which do not do any form of remote server trust verification

        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();


            //non-durable, exclusive, auto-delete queue
//        channel.queueDeclare("rabbitmq-java-test", false, false, false, null);

            channel.basicPublish("", "", null, "<test/>".getBytes());

            System.out.println(channel.isOpen());

//        GetResponse chResponse = channel.basicGet("rabbitmq-java-test", false);
//        if(chResponse == null) {
//            System.out.println("No message retrieved");
//        } else {
//            byte[] body = chResponse.getBody();
//            System.out.println("Recieved: " + new String(body));
//        }


            channel.close();
            conn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
