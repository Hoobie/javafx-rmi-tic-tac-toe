package pl.edu.agh.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.Listener;
import pl.edu.agh.PlayerSign;
import pl.edu.agh.TicTacToe;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Bot implements Listener {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private static final int BOARD_SIZE = 3;

    private final Random random = new Random();
    private final PlayerSign[][] board;

    private TicTacToe ticTacToe;

    public Bot(PlayerSign[][] board, String rmiRegistryIp, String rmiRegistryPort) {
        this.board = board;
        Listener listener;
        try {
            listener = (Listener) UnicastRemoteObject.exportObject(this, 0);
            Naming.rebind("rmi://" + rmiRegistryIp + ":" + rmiRegistryPort + "/bot", listener);
            ticTacToe = (TicTacToe) Naming.lookup("rmi://" + rmiRegistryIp + ":" + rmiRegistryPort + "/tic-tac-toe");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void onMyTurn() throws RemoteException {
        log.debug("Bot's turn.");
        int potentialRow;
        int potentialCol;
        do {
            potentialRow = random.nextInt(BOARD_SIZE);
            potentialCol = random.nextInt(BOARD_SIZE);
        } while (board[potentialRow][potentialCol] != null);
        ticTacToe.endTurn("bot", potentialRow, potentialCol);
    }

    @Override
    public void onOpponentsTurnEnd(int row, int col) throws RemoteException {
    }

    @Override
    public void onWin() throws RemoteException {
    }

    @Override
    public void onLoss() throws RemoteException {
    }

    @Override
    public void onDraw() throws RemoteException {
    }
}
