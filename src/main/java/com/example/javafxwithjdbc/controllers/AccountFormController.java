package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.db.DbException;
import com.example.javafxwithjdbc.listener.DataChangeListener;
import com.example.javafxwithjdbc.model.entities.*;
import com.example.javafxwithjdbc.model.exceptions.ValidationException;
import com.example.javafxwithjdbc.model.services.AccountService;
import com.example.javafxwithjdbc.model.services.ClientService;
import com.example.javafxwithjdbc.utils.Alerts;
import com.example.javafxwithjdbc.utils.Constraints;
import com.example.javafxwithjdbc.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class AccountFormController implements Initializable {

    private Account entity;

    private AccountService accountService;

    private ClientService clientService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private ComboBox<String> comboBoxTypes;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtBalance;

    @FXML
    private DatePicker dpOpeningDate;

    @FXML
    private TextField txtTransferLimit;

    @FXML
    private TextField txtCreditLimit;

    @FXML
    private TextField txtBirthDateAccount;

    @FXML
    private ComboBox<Client> comboBoxClients;

    @FXML
    private Label labelErrorBalance;

    @FXML
    private Label labelErrorOpeningDate;

    @FXML
    private Label labelErrorTransferLimit;

    @FXML
    private Label labelErrorCreditLimit;

    @FXML
    private Label labelErrorBirthDateAccount;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private ObservableList<String> obsTypesList;

    private ObservableList<Client> obsClientsList;

    public void setServices(AccountService accountService, ClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onComboBoxTypeAction(ActionEvent event) {
        if (comboBoxTypes.getSelectionModel().getSelectedItem().equals("simple")) {
            txtCreditLimit.setText("");
            txtBirthDateAccount.setText("");
            txtCreditLimit.setEditable(false);
            txtBirthDateAccount.setEditable(false);
        } else if (comboBoxTypes.getSelectionModel().getSelectedItem().equals("special")) {
            txtBirthDateAccount.setText("");
            txtCreditLimit.setEditable(true);
            txtBirthDateAccount.setEditable(false);
        } else if (comboBoxTypes.getSelectionModel().getSelectedItem().equals("saving")) {
            txtCreditLimit.setText("");
            txtCreditLimit.setEditable(false);
            txtBirthDateAccount.setEditable(true);
        }
    }

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if (accountService == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            accountService.save(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.ShowAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onBtnCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private void setErrorMessages(Map<String, String> error) {
        Set<String> fields = error.keySet();

        labelErrorBalance.setText(fields.contains("balance") ? error.get("balance") : "");

        labelErrorOpeningDate.setText(fields.contains("openingDate") ? error.get("openingDate") : "");

        labelErrorTransferLimit.setText(fields.contains("transferLimit") ? error.get("transferLimit") : "");

        labelErrorCreditLimit.setText(fields.contains("creditLimit") ? error.get("creditLimit") : "");

        labelErrorBirthDateAccount.setText(fields.contains("birthDateAccount") ? error.get("birthDateAccount") : "");
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChange();
        }
    }

    private Account getFormData() {

        ValidationException exception = new ValidationException("Validation error");

        switch (comboBoxTypes.getValue()) {
            case "simple":
                SimpleAccount simpleAccount = new SimpleAccount();

                if (txtBalance.getText() == null || txtBalance.getText().trim().equals("")) {
                    exception.addError("balance", "Field can't be empty");
                }
                simpleAccount.setBalance(Utils.tryParseToDouble(txtBalance.getText()));

                if (dpOpeningDate.getValue() == null) {
                    exception.addError("openingDate", "Field can't be empty");
                } else {
                    simpleAccount.setOpeningDate(dpOpeningDate.getValue());
                }

                if (txtTransferLimit.getText() == null || txtTransferLimit.getText().trim().equals("")) {
                    exception.addError("transferLimit", "Field can't be empty");
                }
                simpleAccount.setTransferLimit(Utils.tryParseToDouble(txtTransferLimit.getText()));

                simpleAccount.setClient(comboBoxClients.getValue());

                return simpleAccount;
            case "special":
                SpecialAccount specialAccount = new SpecialAccount();

                if (txtBalance.getText() == null || txtBalance.getText().trim().equals("")) {
                    exception.addError("balance", "Field can't be empty");
                }
                specialAccount.setBalance(Utils.tryParseToDouble(txtBalance.getText()));

                if (dpOpeningDate.getValue() == null) {
                    exception.addError("openingDate", "Field can't be empty");
                } else {
                    specialAccount.setOpeningDate(dpOpeningDate.getValue());
                }

                if (txtTransferLimit.getText() == null || txtTransferLimit.getText().trim().equals("")) {
                    exception.addError("transferLimit", "Field can't be empty");
                }
                specialAccount.setTransferLimit(Utils.tryParseToDouble(txtTransferLimit.getText()));

                if (txtCreditLimit.getText() == null || txtCreditLimit.getText().trim().equals("")) {
                    exception.addError("creditLimit", "Field can't be empty");
                }
                specialAccount.setCreditLimit(Utils.tryParseToDouble(txtCreditLimit.getText()));

                specialAccount.setClient(comboBoxClients.getValue());

                return specialAccount;
            case "saving":
                SavingAccount savingAccount = new SavingAccount();

                if (txtBalance.getText() == null || txtBalance.getText().trim().equals("")) {
                    exception.addError("balance", "Field can't be empty");
                }
                savingAccount.setBalance(Utils.tryParseToDouble(txtBalance.getText()));

                if (dpOpeningDate.getValue() == null) {
                    exception.addError("openingDate", "Field can't be empty");
                } else {
                    savingAccount.setOpeningDate(dpOpeningDate.getValue());
                }

                if (txtTransferLimit.getText() == null || txtTransferLimit.getText().trim().equals("")) {
                    exception.addError("transferLimit", "Field can't be empty");
                }
                savingAccount.setTransferLimit(Utils.tryParseToDouble(txtTransferLimit.getText()));

                if (txtBirthDateAccount.getText() == null || txtBirthDateAccount.getText().trim().equals("")) {
                    exception.addError("birthDateAccount", "Field can't be empty");
                }
                savingAccount.setBirthDateAccount(Utils.tryParseToInt(txtBirthDateAccount.getText()));

                savingAccount.setClient(comboBoxClients.getValue());

                return savingAccount;
            default:
                return null;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldDouble(txtBalance);
        Constraints.setTextFieldDouble(txtCreditLimit);
        Constraints.setTextFieldDouble(txtTransferLimit);
        Constraints.setTextFieldInteger(txtBirthDateAccount);
        Utils.formatDatePicker(dpOpeningDate, "dd/MM/yyyy");
        initializeComboBoxTypes();
        initializeComboBoxClients();
    }

    private void initializeComboBoxClients() {

        Callback<ListView<Client>, ListCell<Client>> factory = lv -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        comboBoxClients.setCellFactory(factory);
        comboBoxClients.setButtonCell(factory.call(null));
    }

    private void initializeComboBoxTypes() {
        comboBoxTypes.getItems().addAll("simple", "special", "saving");
    }

    public void loadAssociatedObjects() {
        if (clientService == null) {
            throw new IllegalStateException("ClientService was null");
        }
        List<Client> list = clientService.findAll();
        obsClientsList = FXCollections.observableArrayList(list);
        comboBoxClients.setItems(obsClientsList);
    }
}
