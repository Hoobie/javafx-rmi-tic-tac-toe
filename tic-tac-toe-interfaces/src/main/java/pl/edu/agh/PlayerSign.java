package pl.edu.agh;

import java.io.Serializable;

public enum PlayerSign implements Serializable {
    X("X"),
    O("O");

    private String sign;

    PlayerSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
