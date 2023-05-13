package com.example.javafxwithjdbc.controllers;

import com.example.javafxwithjdbc.db.DbException;
import com.example.javafxwithjdbc.listener.DataChangeListener;
import com.example.javafxwithjdbc.model.entities.Client;
import com.example.javafxwithjdbc.model.exceptions.ValidationException;
import com.example.javafxwithjdbc.model.services.ClientService;
import com.example.javafxwithjdbc.utils.Alerts;
import com.example.javafxwithjdbc.utils.Constraints;
import com.example.javafxwithjdbc.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class ClientFormController implements Initializable {

    private ClientService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCpf;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorCpf;

    @FXML
    private Label labelErrorBirthDate;

    public void setClientService(ClientService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            Client entity = getFormData();
            service.save(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
         setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            Alerts.ShowAlert("Error saving client", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChange();
        }
    }

    private Client getFormData() {
        Client obj = new Client();

        ValidationException exception = new ValidationException("Validation error");

        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        if (txtCpf.getText() == null || txtCpf.getText().trim().equals("")) {
            exception.addError("cpf", "Field can't be empty");
        } else if (txtCpf.getText().length() < 11) {
            exception.addError("cpf", "Give a valid CPF");
        }
        obj.setCpf(txtCpf.getText());

        if (dpBirthDate.getValue() == null) {
            exception.addError("birthDate", "Field can't be empty");
        } else {
            obj.setBirthDate(dpBirthDate.getValue());
        }

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtnCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtCpf);
        Constraints.setTextFieldMaxLength(txtCpf, 11);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
    }

    private void setErrorMessages(Map<String, String> error) {
        Set<String> fields = error.keySet();

        labelErrorName.setText(fields.contains("name") ? error.get("name") : "");

        labelErrorCpf.setText(fields.contains("cpf") ? error.get("cpf") : "");

        labelErrorBirthDate.setText(fields.contains("birthDate") ? error.get("birthDate") : "");
    }
}
