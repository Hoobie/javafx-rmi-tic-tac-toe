package pl.edu.agh.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.Listener;
import pl.edu.agh.PlayerSign;

import java.rmi.Naming;

public class Player {
    private static final Logger log = LoggerFactory.getLogger(Player.class);

    public String nick;
    public Listener listener;
    public PlayerSign sign;

    public Player(String nick, PlayerSign sign, String rmiRegistryIp, String rmiRegistryPort) {
        this.nick = nick;
        this.sign = sign;
        try {
            listener = (Listener) Naming.lookup("rmi://" + rmiRegistryIp + ":" + rmiRegistryPort + "/" + nick);
        } catch (Exception e) {
            log.error("Cannot create the player: " + nick);
        }
    }
}
