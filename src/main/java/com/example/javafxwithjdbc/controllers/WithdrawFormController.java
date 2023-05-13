package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.db.DbException;
import com.example.javafxwithjdbc.listener.DataChangeListener;
import com.example.javafxwithjdbc.model.entities.Account;
import com.example.javafxwithjdbc.model.entities.SavingAccount;
import com.example.javafxwithjdbc.model.entities.SimpleAccount;
import com.example.javafxwithjdbc.model.entities.SpecialAccount;
import com.example.javafxwithjdbc.model.exceptions.ValidationException;
import com.example.javafxwithjdbc.model.services.AccountService;
import com.example.javafxwithjdbc.utils.Alerts;
import com.example.javafxwithjdbc.utils.Constraints;
import com.example.javafxwithjdbc.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class WithdrawFormController implements Initializable {

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
    private Label labelCreditLimit;

    @FXML
    private TextField txtWithdraw;

    @FXML
    private Label labelErrorWithdraw;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

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
            if (txtType.getText().equals("simple") || txtType.getText().equals("saving")) {
                if (entity.getBalance() - amount < 0) {
                    Alerts.ShowAlert("Error saving object", null, "Balance cannot be negative", Alert.AlertType.ERROR);
                } else {
                    service.withdraw(entity, amount);
                    notifyDataChangeListeners();
                    Utils.currentStage(event).close();
                }
            } else if (txtType.getText().equals("special")) {
                if (Utils.tryParseToDouble(labelCreditLimit.getText()) + entity.getBalance() - getAmount() < 0) {
                    Alerts.ShowAlert("Error saving object", null, "Balance cannot be negative", Alert.AlertType.ERROR);
                } else {
                    service.withdraw(entity, amount);
                    notifyDataChangeListeners();
                    Utils.currentStage(event).close();
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

    private Double getAmount() {
        ValidationException exception = new ValidationException("Validation error");

        if (txtWithdraw.getText() == null || txtWithdraw.getText().trim().equals("")) {
            exception.addError("withdraw", "Field can't be empty");
        }
        Double amount = Utils.tryParseToDouble(txtWithdraw.getText());

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
        if (entity.getAccount_type().equals("special")) {
            SpecialAccount specialAccount = (SpecialAccount) entity;

            labelCreditLimit.setText(String.valueOf(specialAccount.getCreditLimit()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldDouble(txtWithdraw);
        Constraints.setTextFieldDouble(txtBalance);
    }

    private void setErrorMessages(Map<String, String> error) {
        Set<String> fields = error.keySet();

        if (fields.contains("withdraw")) {
            labelErrorWithdraw.setText(error.get("withdraw"));
        }
    }
}
