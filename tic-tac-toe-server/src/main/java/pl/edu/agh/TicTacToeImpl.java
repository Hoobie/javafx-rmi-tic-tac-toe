package pl.edu.agh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.ai.Bot;
import pl.edu.agh.model.Board;
import pl.edu.agh.model.Player;

import java.rmi.RemoteException;

public class TicTacToeImpl implements TicTacToe {
    private static final Logger log = LoggerFactory.getLogger(TicTacToeImpl.class);

    private final String rmiRegistryIp;
    private final String rmiRegistryPort;

    private Board board = new Board();
    private Player firstPlayer;
    private Player secondPlayer;
    private Bot bot;
    private boolean singlePlayer = false;

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
        if (singlePlayer) {
            this.singlePlayer = true;
            bot = new Bot(this, board);
            secondPlayer = new Player("bot", PlayerSign.O, null, null);
        }
        return playerSign;
    }

    public synchronized void makeTurn(final String nick, int row, int col) throws RemoteException {
        Player player = getPlayer(nick);
        if (player.hasTurn) {
            player.hasTurn = false;
            board.makeTurn(player.sign, row, col);

            PlayerSign winner = board.checkWin();
            if (PlayerSign.X.equals(winner)) {
                firstPlayer.listener.onWin();
                if (!singlePlayer) {
                    secondPlayer.listener.onLoss();
                }
            } else if (PlayerSign.O.equals(winner)) {
                firstPlayer.listener.onLoss();
                if (!singlePlayer) {
                    secondPlayer.listener.onWin();
                }
            }

            if (board.checkDraw()) {
                firstPlayer.listener.onDraw();
                if (!singlePlayer) {
                    secondPlayer.listener.onDraw();
                }
            }

            Player oppositePlayer = getOppositePlayer(player);
            oppositePlayer.hasTurn = true;
            if (singlePlayer && PlayerSign.O.equals(oppositePlayer.sign)) {
                bot.onOpponentsTurnEnd(row, col);
            } else {
                oppositePlayer.listener.onOpponentsTurnEnd(row, col);
            }
            log.info("Player: " + nick + " made a turn.");
        }

    }

    public synchronized void quit(String nick, boolean endGame) throws RemoteException {
        Player player = getPlayer(nick);
        Player oppositePlayer = getOppositePlayer(player);
        log.info("Player: " + nick + " left the server.");
        if (!endGame && !singlePlayer) {
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
