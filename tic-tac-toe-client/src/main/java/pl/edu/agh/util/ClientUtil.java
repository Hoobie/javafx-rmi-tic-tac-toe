package pl.edu.agh.util;

import pl.edu.agh.PlayerSign;

public class ClientUtil {

    public static PlayerSign getOppositeSign(PlayerSign sign) {
        if (sign.equals(PlayerSign.X)) {
            return PlayerSign.O;
        }
        return PlayerSign.X;
    }
}
