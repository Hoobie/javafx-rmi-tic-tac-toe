package pl.edu.agh.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.Listener;
import pl.edu.agh.TicTacToe;
import pl.edu.agh.model.Board;

import java.rmi.RemoteException;
import java.util.Random;

public class Bot implements Listener {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private static final int BOARD_SIZE = 3;

    private final TicTacToe ticTacToe;
    private final Board board;
    private final Random random = new Random();

    public Bot(TicTacToe ticTacToe, Board board) {
        this.ticTacToe = ticTacToe;
        this.board = board;
    }

    public void onOpponentsTurnEnd(int row, int col) throws RemoteException {
        log.debug("Bot's turn.");
        int potentialRow;
        int potentialCol;
        do {
            potentialRow = random.nextInt(BOARD_SIZE);
            potentialCol = random.nextInt(BOARD_SIZE);
        } while (board.getBoard()[potentialRow][potentialCol] != null);
        ticTacToe.makeTurn("bot", potentialRow, potentialCol);
    }

    public void onWin() throws RemoteException {
    }

    public void onLoss() throws RemoteException {
    }

    public void onDraw() throws RemoteException {
    }
}
