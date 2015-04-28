package pl.edu.agh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.model.Bot;
import pl.edu.agh.model.Player;
import pl.edu.agh.util.BoardUtil;

import java.rmi.RemoteException;

public class TicTacToeImpl implements TicTacToe {
    private static final Logger log = LoggerFactory.getLogger(TicTacToeImpl.class);

    private static final int BOARD_SIZE = 3;

    private final String rmiRegistryIp;
    private final String rmiRegistryPort;

    private PlayerSign[][] board = new PlayerSign[BOARD_SIZE][BOARD_SIZE];
    private Player firstPlayer;
    private Player secondPlayer;

    public TicTacToeImpl(String rmiRegistryIp, String rmiRegistryPort) {
        this.rmiRegistryIp = rmiRegistryIp;
        this.rmiRegistryPort = rmiRegistryPort;

        new Thread() {
            @Override
            public void run() {
                try {
                    gameLoop();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        }.start();
    }

    private void gameLoop() throws Exception {
        while (true) {
            Thread.sleep(100);
            if (firstPlayer != null && secondPlayer != null) {
                firstPlayer.listener.onMyTurn();
                while (true) {
                    Turn turn = firstPlayer.listener.getMyTurn();
                    if (turn != null) {
                        endTurn(firstPlayer.nick, turn.row, turn.col);
                        break;
                    }
                }

                secondPlayer.listener.onMyTurn();
                while (true) {
                    Turn turn = secondPlayer.listener.getMyTurn();
                    if (turn != null) {
                        endTurn(secondPlayer.nick, turn.row, turn.col);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public PlayerSign join(String nick, boolean singlePlayer) throws RemoteException {
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

        initSinglePlayer(singlePlayer);

        return playerSign;
    }

    private void initSinglePlayer(boolean singlePlayer) {
        if (singlePlayer) {
            new Bot(board, rmiRegistryIp, rmiRegistryPort);
            secondPlayer = new Player("bot", PlayerSign.O, rmiRegistryIp, rmiRegistryPort);
            log.info("Bot created.");
        }
    }

    @Override
    public void endTurn(final String nick, int row, int col) throws RemoteException {
        Player player = getPlayer(nick);
        Player oppositePlayer = getOppositePlayer(player);

        board[row][col] = player.sign;
        checkForGameEvents();

        oppositePlayer.listener.onOpponentsTurnEnd(row, col);

        log.info("Player: " + nick + " has made a turn.");
    }

    private void checkForGameEvents() throws RemoteException {
        PlayerSign winner = BoardUtil.checkWin(board);
        if (PlayerSign.X.equals(winner)) {
            firstPlayer.listener.onWin();
            secondPlayer.listener.onLoss();
            return;
        } else if (PlayerSign.O.equals(winner)) {
            firstPlayer.listener.onLoss();
            secondPlayer.listener.onWin();
            return;
        }

        if (BoardUtil.checkDraw(board)) {
            firstPlayer.listener.onDraw();
            secondPlayer.listener.onDraw();
        }
    }

    @Override
    public void quit(String nick, boolean endGame) throws RemoteException {
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
