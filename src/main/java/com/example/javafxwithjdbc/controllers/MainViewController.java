package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.Main;
import com.example.javafxwithjdbc.model.services.AccountService;
import com.example.javafxwithjdbc.model.services.ClientService;
import com.example.javafxwithjdbc.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemClient;

    @FXML
    private MenuItem menuItemAccount;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemAboutAction() {
        loadView("About.fxml", x -> {});
    }

    @FXML
    public void onMenuItemClientAction() {
        loadView("ClientList.fxml", (ClientListController controller) -> {
            controller.setClientService(new ClientService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAccountAction() {
        loadView("AccountList.fxml", (AccountListController controller) -> {
            controller.setAccountService(new AccountService());
            controller.updateTableView();
        });
    }

    @Override
    public void initialize(URL uri, ResourceBundle resourceBundle) {

    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            VBox newVBox = fxmlLoader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = fxmlLoader.getController();
            initializingAction.accept(controller);

        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
