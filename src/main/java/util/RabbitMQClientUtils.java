package util;

public class RabbitMQClientUtils {

    public final static String DEFAULT_PLACEORDER_PAYLOAD =
            "<m:placeOrder xmlns:m=\"http://services.samples\">\n" +
                    "\t<m:order>\n" +
                    "\t\t<m:price>100</m:price>\n" +
                    "\t\t<m:quantity>20</m:quantity>\n" +
                    "\t\t<m:symbol>RMQ</m:symbol>\n" +
                    "\t</m:order>\n" +
                    "</m:placeOrder>";


}
