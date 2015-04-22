package pl.edu.agh.model;

import pl.edu.agh.PlayerSign;

public class Board {
    private static final int BOARD_SIZE = 3;

    private PlayerSign[][] board = new PlayerSign[BOARD_SIZE][BOARD_SIZE];

    public void makeTurn(PlayerSign sign, int row, int col) {
        board[row][col] = sign;
    }

    public boolean playerHasWon(PlayerSign sign) {
        // TODO: implement
        return false;
    }
}
