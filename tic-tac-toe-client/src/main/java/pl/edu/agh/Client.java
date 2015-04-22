package pl.edu.agh;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.RemoteException;

import static pl.edu.agh.util.ClientUtil.isMyTurn;

public class Client extends Application {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static final int BOARD_SIZE = 3;

    private static TicTacToe ticTacToe = null;
    private static String nick;
    private static PlayerSign mySign;
    private static boolean myTurn;

    public static void main(String[] args) throws Exception {
        ticTacToe = (TicTacToe) Naming.lookup("rmi://127.0.0.1:1099/tic-tac-toe");
        nick = args[0];
        mySign = ticTacToe.join(nick, false);
        myTurn = isMyTurn(mySign);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        log.info("Starting application...");

        String fxmlFile = "/fxml/hello.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");

        final GridPane gridPane = (GridPane) scene.lookup("#gridPane");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                final Button button = new Button();
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        onButtonClick(button);
                    }
                });
                gridPane.add(button, i, j);
            }
        }

        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();
    }

    private void onButtonClick(Button button) {
        if (myTurn) {
            int row = GridPane.getRowIndex(button);
            int col = GridPane.getColumnIndex(button);
            try {
                ticTacToe.makeTurn(nick, row, col);
            } catch (RemoteException e) {
                log.error(e.getMessage());
            }
            button.setText(mySign.getSign());
        }
    }
}
