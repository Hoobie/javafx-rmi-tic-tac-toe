package pl.edu.agh;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.util.ClientUtil;

import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import static pl.edu.agh.util.SceneUtil.makeEndDialog;

public class Client extends Application implements Listener {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static final String PROPERTIES_FILE_NAME = "client.properties";
    private static final int BOARD_SIZE = 3;

    private static Properties properties = new Properties();

    private Scene scene;

    private String nick;
    private boolean singlePlayer = false;
    private TicTacToe ticTacToe = null;
    private PlayerSign mySign;
    private boolean myTurn;
    private Turn lastTurn;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/client.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");

        final GridPane gridPane = (GridPane) scene.lookup("#gridPane");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                final Button button = new Button();
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setOnMouseClicked(event -> onButtonClick(button));
                gridPane.add(button, i, j);
            }
        }

        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(we -> {
            try {
                ticTacToe.quit(nick, false);
                Platform.exit();
            } catch (RemoteException e) {
                log.error(e.getMessage());
            }
        });

        makeNickDialog(stage);
    }

    private void makeNickDialog(Stage primaryStage) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);

        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);

        final TextField textField = new TextField("Nick");
        dialogVBox.getChildren().add(textField);

        final CheckBox checkBox = new CheckBox("Single player");
        dialogVBox.getChildren().add(checkBox);

        Button button = new Button("OK");
        button.setOnMouseClicked(event -> {
            String text = textField.getText();
            if (StringUtils.isNotBlank(text)) {
                nick = textField.getText();
                singlePlayer = checkBox.isSelected();
                initClient();
                dialog.close();
            }
        });
        dialogVBox.getChildren().add(button);

        Scene dialogScene = new Scene(dialogVBox, 240, 120);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void initClient() {
        InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        try {
            properties.load(input);
            String rmiRegistryIp = properties.getProperty("rmiRegistryIp");
            String rmiRegistryPort = properties.getProperty("rmiRegistryPort");

            Listener listener = (Listener) UnicastRemoteObject.exportObject(this, 0);
            Naming.rebind("rmi://" + rmiRegistryIp + ":" + rmiRegistryPort + "/" + nick, listener);

            ticTacToe = (TicTacToe) Naming.lookup("rmi://" + rmiRegistryIp + ":" + rmiRegistryPort + "/tic-tac-toe");

            mySign = ticTacToe.join(nick, singlePlayer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Player: " + nick + " joined the server with the sign: " + mySign.getSign());
    }

    private void onButtonClick(Button button) {
        if (myTurn) {
            int row = GridPane.getRowIndex(button);
            int col = GridPane.getColumnIndex(button);
            button.setText(mySign.getSign());
            try {
                myTurn = false;
                lastTurn = new Turn(row, col);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void onMyTurn() throws RemoteException {
        log.debug("Player's turn.");
        lastTurn = null;
        myTurn = true;
    }

    @Override
    public Turn getMyTurn() throws RemoteException {
        return lastTurn;
    }

    @Override
    public void onOpponentsTurnEnd(int row, int col) throws RemoteException {
        // inverted axises
        final Button button = (Button) getNodeFromGridPane(col, row);
        if (button != null) {
            Platform.runLater(() -> button.setText(ClientUtil.getOppositeSign(mySign).getSign()));
        } else {
            log.error("No such node.");
        }
    }

    private Node getNodeFromGridPane(int col, int row) {
        GridPane gridPane = (GridPane) scene.lookup("#gridPane");
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    @Override
    public void onWin() throws RemoteException {
        myTurn = false;
        Platform.runLater(() -> makeEndDialog(scene.getWindow(), ticTacToe, nick, "You've won!"));
    }

    @Override
    public void onLoss() throws RemoteException {
        myTurn = false;
        Platform.runLater(() -> makeEndDialog(scene.getWindow(), ticTacToe, nick, "You've loose."));
    }

    @Override
    public void onDraw() throws RemoteException {
        myTurn = false;
        Platform.runLater(() -> makeEndDialog(scene.getWindow(), ticTacToe, nick, "Draw."));
    }
}
