package pl.edu.agh;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import pl.edu.agh.model.Board;
import pl.edu.agh.model.Player;

import java.rmi.RemoteException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class TicTacToeImpl implements TicTacToe {

    private static List<Player> players = newArrayList();
    private static Board board = new Board();

    public synchronized PlayerSign join(String nick, boolean singlePlayer) throws RemoteException {
        PlayerSign playerSign = PlayerSign.X;
        if (players.size() > 0) {
            playerSign = PlayerSign.O;
        }
        Player player = new Player(nick, playerSign);
        players.add(player);
        return playerSign;
    }

    public synchronized void makeTurn(final String nick, int row, int col) throws RemoteException {
        Player player = Iterables.find(players, new Predicate<Player>() {
            public boolean apply(Player player) {
                return player.nick.equals(nick);
            }
        });
        if (player.hasTurn) {
            board.makeTurn(player.sign, row, col);
        }
        // TODO: notify about turn/win/loss
    }
}
