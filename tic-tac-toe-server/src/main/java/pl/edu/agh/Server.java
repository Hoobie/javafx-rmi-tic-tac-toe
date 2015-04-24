package pl.edu.agh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private static final String PROPERTIES_FILE_NAME = "server.properties";

    private static Properties properties = new Properties();

    public static void main(String[] args) {
        try {
            InputStream input = Server.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
            properties.load(input);
            String rmiRegistryIp = properties.getProperty("rmiRegistryIp");
            String rmiRegistryPort = properties.getProperty("rmiRegistryPort");

            TicTacToe ticTacToe = (TicTacToe) UnicastRemoteObject.exportObject(new TicTacToeImpl(rmiRegistryIp, rmiRegistryPort), 0);
            Naming.rebind("rmi://" + rmiRegistryIp + ":" + rmiRegistryPort + "/tic-tac-toe", ticTacToe);
            log.info("Server started.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
