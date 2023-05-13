package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.Main;
import com.example.javafxwithjdbc.listener.DataChangeListener;
import com.example.javafxwithjdbc.model.entities.Account;
import com.example.javafxwithjdbc.model.services.AccountService;
import com.example.javafxwithjdbc.model.services.ClientService;
import com.example.javafxwithjdbc.utils.Alerts;
import com.example.javafxwithjdbc.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AccountListController implements Initializable, DataChangeListener {

   private AccountService service;

   @FXML
   private TableView<Account> tableViewAccount;

   @FXML
   private TableColumn<Account, Integer> tableColumnId;

   @FXML
   private TableColumn<Account, String> tableColumnType;

   @FXML
   private TableColumn<Account, Double> tableColumnBalance;

   @FXML
   private TableColumn<Account, LocalDate> tableColumnOpeningDate;

   @FXML
   private TableColumn<Account, Double> tableColumnTransferLimit;

   @FXML
   private TableColumn<Account, Double> tableColumnCreditLimit;

   @FXML
   private TableColumn<Account, LocalDate> tableColumnBirthDateAccount;

   @FXML
   private TableColumn<Account, Account> tableColumnWithdraw;

   @FXML
   private TableColumn<Account, Account> tableColumnDeposit;

   @FXML
   private TableColumn<Account, Account> tableColumnTransfer;

   @FXML
   private Button btNew;

   private ObservableList<Account> obsList;

   @FXML
   public void onBtNewAction(ActionEvent event) {
       Stage parentStage = Utils.currentStage(event);
       createDialogForm("AccountForm.fxml", parentStage);
   }

   public void setAccountService(AccountService service) {
        this.service = service;
   }

    private void createDialogForm(String absoluteName, Stage parentStage) {
       try {
           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
           Pane pane = fxmlLoader.load();

           AccountFormController controller = fxmlLoader.getController();
           controller.setServices(new AccountService(), new ClientService());
           controller.loadAssociatedObjects();
           controller.subscribeDataChangeListener(this);

           Stage dialogStage = new Stage();
           dialogStage.setTitle("Enter Account data");
           dialogStage.setScene(new Scene(pane));
           dialogStage.setResizable(false);
           dialogStage.initOwner(parentStage);
           dialogStage.initModality(Modality.WINDOW_MODAL);
           dialogStage.showAndWait();

       } catch (IOException e) {
           Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
       }

    }


    @Override
    public void onDataChange() {
        updateTableView();
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Account> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewAccount.setItems(obsList);
        initWithdrawButtons();
        initDepositButtons();
        initTransferButtons();
    }

    private void initWithdrawButtons() {
        tableColumnWithdraw.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        tableColumnWithdraw.setCellFactory(param -> new TableCell<Account, Account>(){

            private final Button button = new Button("Withdraw");

            @Override
            protected void updateItem(Account obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createWithdrawForm(obj, "WithdrawForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initTransferButtons() {
        tableColumnTransfer.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        tableColumnTransfer.setCellFactory(param -> new TableCell<Account, Account>(){

            private final Button button = new Button("Transfer");

            @Override
            protected void updateItem(Account obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createTransferForm(obj, "TransferForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initDepositButtons() {
        tableColumnDeposit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        tableColumnDeposit.setCellFactory(param -> new TableCell<Account, Account>(){

            private final Button button = new Button("Deposit");

            @Override
            protected void updateItem(Account obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDepositForm(obj, "DepositForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void createDepositForm(Account obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            DepositFormController controller = fxmlLoader.getController();
            controller.setAccount(obj);
            controller.setService(new AccountService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Deposit data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void createWithdrawForm(Account obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            WithdrawFormController controller = fxmlLoader.getController();
            controller.setAccount(obj);
            controller.setService(new AccountService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Withdraw data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void createTransferForm(Account obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            TransferFormController controller = fxmlLoader.getController();
            controller.setAccount(obj);
            controller.setService(new AccountService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            controller.loadAssociatedObjects();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Transfer data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            Alerts.ShowAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("account_type"));

        tableColumnBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        Utils.formatTableColumnDouble(tableColumnBalance, 2);

        tableColumnOpeningDate.setCellValueFactory(new PropertyValueFactory<>("openingDate"));
        Utils.formatTableColumnDate(tableColumnOpeningDate, "dd/MM/yyyy");

        tableColumnTransferLimit.setCellValueFactory(new PropertyValueFactory<>("transferLimit"));
        Utils.formatTableColumnDouble(tableColumnTransferLimit, 2);

        tableColumnCreditLimit.setCellValueFactory(new PropertyValueFactory<>("creditLimit"));
//        Utils.formatTableColumnDouble(tableColumnCreditLimit, 2);

        tableColumnBirthDateAccount.setCellValueFactory(new PropertyValueFactory<>("birthDateAccount"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewAccount.prefHeightProperty().bind(stage.heightProperty());
    }
}
