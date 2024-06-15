package lk.ijse.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class HomePageFormController {
    @FXML
    public AnchorPane dashboardPane;

    private AnchorPane centerNode;

    public void setCenterNode(AnchorPane centerNode) {
        this.centerNode = centerNode;
    }
}
