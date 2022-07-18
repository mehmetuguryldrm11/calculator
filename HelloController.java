package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class HelloController {

    @FXML
    private Label result;
    private long number1 = 0;
    private String operator = "";
    private boolean start = true;
    private MainModal mainModal = new MainModal();

    @FXML
    protected void processNumbers(ActionEvent event) {
        if(start){
            result.setText("");
            start = false;
        }
        String value = ((Button)event.getSource()).getText();
        result.setText(result.getText()+value);
    }

    @FXML
    protected void processOperators(ActionEvent event) {
        String value = ((Button)event.getSource()).getText();
        if(!value.equals("=")){
            if(!operator.isEmpty())
                return;
            operator = value;
            number1 = Long.parseLong(result.getText());
            result.setText("");
        } else{
            if(operator.isEmpty())
                return;
            long number2 = Long.parseLong(result.getText());
            float output = mainModal.calculate(number1, number2, operator);
            result.setText(String.valueOf(output));
            operator = "";
            start = true;
        }
    }
}