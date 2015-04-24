package pl.edu.agh.model;

import pl.edu.agh.PlayerSign;

public class Board {
    private static final int BOARD_SIZE = 3;

    private PlayerSign[][] board = new PlayerSign[BOARD_SIZE][BOARD_SIZE];

    public void makeTurn(PlayerSign sign, int row, int col) {
        board[row][col] = sign;
    }

    public PlayerSign checkWin() {
        PlayerSign rowsAndColumns = checkRowsAndColumns();
        if (rowsAndColumns == null) {
            PlayerSign cross1 = checkCross1();
            if (cross1 == null) {
                return checkCross2();
            }
            return cross1;
        }
        return rowsAndColumns;
    }

    private PlayerSign checkRowsAndColumns() {
        int firstRowCounter = 0;
        boolean firstRowStreak = false;
        int firstColumnCounter = 0;
        boolean firstColumnStreak = false;

        int secondRowCounter = 0;
        boolean secondRowStreak = false;
        int secondColumnCounter = 0;
        boolean secondColumnStreak = false;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (PlayerSign.X.equals(board[i][j])) {
                    if (firstRowStreak) {
                        firstRowCounter++;
                    } else {
                        firstRowCounter = 1;
                        firstRowStreak = true;
                        secondRowStreak = false;
                        secondRowCounter = 0;
                    }
                }
                if (PlayerSign.X.equals(board[j][i])) {
                    if (firstColumnStreak) {
                        firstColumnCounter++;
                    } else {
                        firstColumnCounter = 1;
                        firstColumnStreak = true;
                        secondColumnStreak = false;
                        secondColumnCounter = 0;
                    }
                }
                if (PlayerSign.O.equals(board[i][j])) {
                    if (secondRowStreak) {
                        secondRowCounter++;
                    } else {
                        secondRowCounter = 1;
                        secondRowStreak = true;
                        firstRowStreak = false;
                        firstRowCounter = 0;
                    }
                }
                if (PlayerSign.O.equals(board[j][i])) {
                    if (secondColumnStreak) {
                        secondColumnCounter++;
                    } else {
                        secondColumnCounter = 1;
                        secondColumnStreak = true;
                        firstColumnStreak = false;
                        firstColumnCounter = 0;
                    }
                }
                if (board[i][j] == null) {
                    firstRowStreak = false;
                    secondRowStreak = false;
                    firstRowCounter = 0;
                    secondRowCounter = 0;
                }
                if (board[j][i] == null) {
                    firstColumnStreak = false;
                    secondColumnStreak = false;
                    firstColumnCounter = 0;
                    secondColumnCounter = 0;
                }
            }
            if (firstRowCounter == BOARD_SIZE || firstColumnCounter == BOARD_SIZE) {
                return PlayerSign.X;
            } else if (secondRowCounter == BOARD_SIZE || secondColumnCounter == BOARD_SIZE) {
                return PlayerSign.O;
            }
            firstRowStreak = false;
            firstColumnStreak = false;
            secondRowStreak = false;
            secondColumnStreak = false;
            firstRowCounter = 0;
            secondRowCounter = 0;
            firstColumnCounter = 0;
            secondColumnCounter = 0;
        }
        return null;
    }

    private PlayerSign checkCross1() {
        boolean firstStreak = false;
        boolean secondStreak = false;
        int firstCounter = 0;
        int secondCounter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (PlayerSign.X.equals(board[i][i])) {
                if (firstStreak) {
                    firstCounter++;
                } else {
                    firstCounter = 1;
                    firstStreak = true;
                    secondCounter = 0;
                    secondStreak = false;
                }
            } else if (PlayerSign.O.equals(board[i][i])) {
                if (secondStreak) {
                    secondCounter++;
                } else {
                    secondCounter = 1;
                    secondStreak = true;
                    firstCounter = 0;
                    firstStreak = false;
                }
            } else {
                return null;
            }
        }
        if (firstCounter == BOARD_SIZE) {
            return PlayerSign.X;
        } else if (secondCounter == BOARD_SIZE) {
            return PlayerSign.O;
        }
        return null;
    }

    private PlayerSign checkCross2() {
        boolean firstStreak = false;
        boolean secondStreak = false;
        int firstCounter = 0;
        int secondCounter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (PlayerSign.X.equals(board[i][BOARD_SIZE - i - 1])) {
                if (firstStreak) {
                    firstCounter++;
                } else {
                    firstCounter = 1;
                    firstStreak = true;
                    secondCounter = 0;
                    secondStreak = false;
                }
            } else if (PlayerSign.O.equals(board[i][BOARD_SIZE - i - 1])) {
                if (secondStreak) {
                    secondCounter++;
                } else {
                    secondCounter = 1;
                    secondStreak = true;
                    firstCounter = 0;
                    firstStreak = false;
                }
            } else {
                return null;
            }
        }
        if (firstCounter == BOARD_SIZE) {
            return PlayerSign.X;
        } else if (secondCounter == BOARD_SIZE) {
            return PlayerSign.O;
        }
        return null;
    }

    public boolean checkDraw() {
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    counter++;
                }
            }
        }
        return counter == BOARD_SIZE * BOARD_SIZE;
    }
}
