package pl.edu.agh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.model.Board;
import pl.edu.agh.model.Player;

import java.rmi.RemoteException;

public class TicTacToeImpl implements TicTacToe {
    private static final Logger log = LoggerFactory.getLogger(TicTacToeImpl.class);

    private final String rmiRegistryIp;
    private final String rmiRegistryPort;

    private Player firstPlayer;
    private Player secondPlayer;
    private Board board = new Board();

    public TicTacToeImpl(String rmiRegistryIp, String rmiRegistryPort) {
        this.rmiRegistryIp = rmiRegistryIp;
        this.rmiRegistryPort = rmiRegistryPort;
    }

    public synchronized PlayerSign join(String nick, boolean singlePlayer) throws RemoteException {
        PlayerSign playerSign = PlayerSign.X;
        if (firstPlayer != null) {
            playerSign = PlayerSign.O;
        }
        Player player = new Player(nick, playerSign, rmiRegistryIp, rmiRegistryPort);
        if (PlayerSign.X.equals(player.sign)) {
            firstPlayer = player;
        } else {
            secondPlayer = player;
        }
        log.info("Player: " + nick + " joined the server with the sign: " + playerSign);
        return playerSign;
    }

    public synchronized void makeTurn(final String nick, int row, int col) throws RemoteException {
        Player player = getPlayer(nick);
        if (player.hasTurn) {
            player.hasTurn = false;
            board.makeTurn(player.sign, row, col);
            Player oppositePlayer = getOppositePlayer(player);
            oppositePlayer.hasTurn = true;
            oppositePlayer.listener.onOpponentsTurnEnd(row, col);
            log.info("Player: " + nick + " made a turn.");

            PlayerSign winner = board.checkWin();
            if (PlayerSign.X.equals(winner)) {
                firstPlayer.listener.onWin();
                secondPlayer.listener.onLoss();
                return;
            } else if (PlayerSign.O.equals(winner)) {
                firstPlayer.listener.onLoss();
                secondPlayer.listener.onWin();
                return;
            }

            if (board.checkDraw()) {
                firstPlayer.listener.onDraw();
                secondPlayer.listener.onDraw();
            }
        }
    }

    public synchronized void quit(String nick, boolean endGame) throws RemoteException {
        Player player = getPlayer(nick);
        Player oppositePlayer = getOppositePlayer(player);
        log.info("Player: " + nick + " left the server.");
        if (!endGame) {
            oppositePlayer.listener.onWin();
        }
    }

    private Player getPlayer(String nick) {
        if (nick.equals(firstPlayer.nick)) {
            return firstPlayer;
        }
        return secondPlayer;
    }

    private Player getOppositePlayer(Player player) {
        if (PlayerSign.X.equals(player.sign)) {
            return secondPlayer;
        }
        return firstPlayer;
    }
}
