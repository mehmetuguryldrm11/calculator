package cl.eye;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    private Label loginMessageLabel;
    @FXML
    private ImageView brandImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandFile = new File("Images/Bigbulb3-230x410.png");
        Image brandImage = new Image(brandFile.toURI().toString());
        brandImageView.setImage(brandImage);

        File lockFile = new File("Images/646003-200.png");
        Image lockImage = new Image(brandFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void loginButtonOnAction(ActionEvent event) {
        loginMessageLabel.setText("You try to login?");
        if (usernameTextField.getText().isEmpty() == false && enterPasswordField.getText().isEmpty() == false) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter the username and password");
        }
    }


    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectionDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = " + usernameTextField.getText() +  "AND PASSWORD = " + enterPasswordField.getText() + "";

        try {
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    loginMessageLabel.setText("Congratulations!");
                } else{
                    loginMessageLabel.setText("Invalid Login! Please try again later.");

                }

            }
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
