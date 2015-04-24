package pl.edu.agh.util;

import pl.edu.agh.PlayerSign;

public class ClientUtil {

    public static boolean isMyTurn(PlayerSign sign) {
        return sign.equals(PlayerSign.X);
    }

    public static PlayerSign getOppositeSign(PlayerSign sign) {
        if (sign.equals(PlayerSign.X)) {
            return PlayerSign.O;
        }
        return PlayerSign.X;
    }
}
