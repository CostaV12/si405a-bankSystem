package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.Main;
import com.example.javafxwithjdbc.model.services.DepartmentService;
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

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("onMenuItemSellerAction");
    }

    @FXML
    public void onMenuItemDepartmentAction() {
        loadView2("DepartmentList.fxml");
    }

    @FXML
    public void onMenuItemAboutAction() {
        loadView("About.fxml");
    }

    @Override
    public void initialize(URL uri, ResourceBundle resourceBundle) {

    }

    private synchronized void loadView(String absoluteName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            VBox newVBox = fxmlLoader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private synchronized void loadView2(String absoluteName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            VBox newVBox = fxmlLoader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            DepartmentListController controller = fxmlLoader.getController();
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();

        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
