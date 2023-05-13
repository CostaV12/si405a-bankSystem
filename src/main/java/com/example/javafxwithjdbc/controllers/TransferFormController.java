package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.db.DbException;
import com.example.javafxwithjdbc.listener.DataChangeListener;
import com.example.javafxwithjdbc.model.entities.*;
import com.example.javafxwithjdbc.model.exceptions.ValidationException;
import com.example.javafxwithjdbc.model.services.AccountService;
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

public class TransferFormController implements Initializable {

    private Account entity;

    private AccountService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtType;

    @FXML
    private TextField txtBalance;

    @FXML
    private TextField txtTransferLimit;

    @FXML
    private TextField txtAmount;

    @FXML
    private Label labelCreditLimit;

    @FXML
    private Label labelErrorAmount;

    @FXML
    private ComboBox<Account> comboBoxAccount;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    private ObservableList<Account> obsList;

    public void setAccount(Account entity) {
        this.entity = entity;
    }

    public void setService(AccountService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnConfirmAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            Double amount = getAmount();
            Account accountDebtor = getDebtor();

            if (amount > Utils.tryParseToDouble(txtTransferLimit.getText())) {
                Alerts.ShowAlert("Error saving object", null, "Amount is greater than transfer limit", Alert.AlertType.ERROR);
            } else {
                if (txtType.getText().equals("simple") || txtType.getText().equals("saving")) {
                    if (entity.getBalance() - amount < 0) {
                        Alerts.ShowAlert("Error saving object", null, "Balance cannot be negative", Alert.AlertType.ERROR);
                    } else {
                        service.transfer(entity, accountDebtor, amount);
                        notifyDataChangeListeners();
                        Utils.currentStage(event).close();
                    }
                } else if (txtType.getText().equals("special")) {
                    if (Utils.tryParseToDouble(labelCreditLimit.getText()) + entity.getBalance() - getAmount() < 0) {
                        Alerts.ShowAlert("Error saving object", null, "Balance cannot be negative", Alert.AlertType.ERROR);
                    } else {
                        service.transfer(entity, accountDebtor, amount);
                        notifyDataChangeListeners();
                        Utils.currentStage(event).close();
                    }
                }
            }
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.ShowAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtnCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private Account getFormData() {
        Account obj;

        switch (txtType.getText()) {
            case "simple":
                obj = new SimpleAccount();
                break;
            case "special":
                obj = new SpecialAccount();
                break;
            case "saving":
                obj = new SavingAccount();
                break;
            default:
                throw new RuntimeException("Error");
        }

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setAccount_type(txtType.getText());
        obj.setBalance(Utils.tryParseToDouble(txtBalance.getText()));

        return obj;
    }

    private Account getDebtor() {
        return comboBoxAccount.getValue();
    }

    private Double getAmount() {
        ValidationException exception = new ValidationException("Validation error");

        if (txtAmount.getText() == null || txtAmount.getText().trim().equals("")) {
            exception.addError("amount", "Field can't be empty");
        }
        Double amount = Utils.tryParseToDouble(txtAmount.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return amount;
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChange();
        }
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtType.setText(entity.getAccount_type());
        txtBalance.setText(String.valueOf(entity.getBalance()));
        txtTransferLimit.setText(String.valueOf(entity.getTransferLimit()));
        if (entity.getAccount_type().equals("special")) {
            labelCreditLimit.setText(String.valueOf(((SpecialAccount) entity).getCreditLimit()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldDouble(txtAmount);
        Constraints.setTextFieldDouble(txtBalance);
        Constraints.setTextFieldDouble(txtTransferLimit);
        initializeComboBoxAccount();
    }

    private void setErrorMessages(Map<String, String> error) {
        Set<String> fields = error.keySet();

        if (fields.contains("amount")) {
            labelErrorAmount.setText(error.get("amount"));
        }
    }

    public void loadAssociatedObjects() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Account> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxAccount.setItems(obsList);
    }

    private void initializeComboBoxAccount() {
        Callback<ListView<Account>, ListCell<Account>> factory = lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.valueOf(item.getId()));
            }
        };

        comboBoxAccount.setCellFactory(factory);
        comboBoxAccount.setButtonCell(factory.call(null));
    }
}
