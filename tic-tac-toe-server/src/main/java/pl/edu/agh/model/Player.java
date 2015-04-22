package pl.edu.agh.model;

import pl.edu.agh.PlayerSign;

public class Player {
    public String nick;
    public PlayerSign sign;
    public boolean hasTurn;

    public Player(String nick, PlayerSign sign) {
        this.nick = nick;
        this.sign = sign;
        this.hasTurn = sign.equals(PlayerSign.X);
    }
}
