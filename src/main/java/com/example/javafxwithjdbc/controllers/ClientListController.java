package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.Main;
import com.example.javafxwithjdbc.listener.DataChangeListener;
import com.example.javafxwithjdbc.model.entities.Client;
import com.example.javafxwithjdbc.model.services.ClientService;
import com.example.javafxwithjdbc.utils.Alerts;
import com.example.javafxwithjdbc.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class ClientListController implements Initializable, DataChangeListener {

    private ClientService service;

    @FXML
    private TableView<Client> tableViewClient;

    @FXML
    private TableColumn<Client, Integer> tableColumnId;

    @FXML
    private TableColumn<Client, String> tableColumnName;

    @FXML
    private TableColumn<Client, String> tableColumnCpf;

    @FXML
    private TableColumn<Client, LocalDate> tableColumnBirthDate;

    @FXML
    private Button btNew;

    private ObservableList<Client> obsList;

    @FXML
    public void onBtnNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Client obj = new Client();
        createDialogForm(obj, "ClientForm.fxml", parentStage);
    }

    public void setClientService(ClientService service) {
        this.service = service;
    }

    @Override
    public void onDataChange() {
        updateTableView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewClient.prefHeightProperty().bind(stage.heightProperty());
    }

    private void createDialogForm(Client obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            ClientFormController controller = fxmlLoader.getController();
            controller.setClientService(new ClientService());
            controller.subscribeDataChangeListener(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Client data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();


        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Client> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewClient.setItems(obsList);
    }
}
