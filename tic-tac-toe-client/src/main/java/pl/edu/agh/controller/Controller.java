package pl.edu.agh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @FXML private javafx.scene.control.MenuBar menuBar;

    @FXML
    public void close(ActionEvent event) {
        log.info("Client closed.");
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
}
