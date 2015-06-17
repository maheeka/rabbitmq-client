package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RabbitMQBrokerUtils {

    private final static String RABBITMQ_HOME_PATH = "/Users/maheeka/ESB_WORK/RABBITMQ/rabbitmq_server-3.5.0/sbin";
    private final static File rabbitMQHome = new File(RABBITMQ_HOME_PATH);
    private static final Log log = LogFactory.getLog(RabbitMQBrokerUtils.class);

    public static void main(String[] args) throws IOException {
        shutdownBroker();
        startupBroker();
        resetBroker();
    }

    private static void shutdownBroker() throws IOException {
        execute("sh rabbitmqctl stop");
    }

    private static void startupBroker() throws IOException {
        execute("sh rabbitmq-server -detached");
    }

    private static void resetBroker() {
        execute("sh rabbitmqctl stop_app");
        execute("sh rabbitmqctl reset");
        execute("sh rabbitmqctl start_app");
    }

    private static void execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command, null, rabbitMQHome);
            InputStream instream = process.getInputStream();
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(instream));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            reader.close();
            instream.close();
            log.info(writer.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
